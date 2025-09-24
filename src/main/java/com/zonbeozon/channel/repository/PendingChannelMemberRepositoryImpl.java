package com.zonbeozon.channel.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.PendingChannelMemberDto;
import com.zonbeozon.channel.entity.PendingChannelMember;
import com.zonbeozon.image.dto.ImageDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QBannedChannelMember.bannedChannelMember;
import static com.zonbeozon.channel.entity.QPendingChannelMember.pendingChannelMember;
import static com.zonbeozon.image.entity.QImage.image;
import static com.zonbeozon.member.domain.QMember.member;
import static com.zonbeozon.member.domain.QMemberProfile.memberProfile;

@Repository
@RequiredArgsConstructor
public class PendingChannelMemberRepositoryImpl implements PendingChannelMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public Page<PendingChannelMemberDto> findByChannelId(Long channelId, Pageable pageable) {
        List<PendingChannelMemberDto> content = queryFactory
                .select(Projections.constructor(PendingChannelMemberDto.class,
                        member.id,
                        member.username,
                        new CaseBuilder()
                                .when(memberProfile.isNotNull())
                                .then(image.id)
                                .otherwise((Long) null),
                        new CaseBuilder()
                                .when(memberProfile.isNotNull())
                                .then(image.url)
                                .otherwise((String) null),
                        member.role,
                        pendingChannelMember.requestedAt
                ))
                .from(pendingChannelMember)
                .join(pendingChannelMember.member, member)
                .leftJoin(member.profile, memberProfile)
                .leftJoin(memberProfile.image, image)
                .where(pendingChannelMember.channel.id.eq(channelId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(pendingChannelMember.count())
                .from(pendingChannelMember)
                .where(pendingChannelMember.channel.id.eq(channelId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        if (sort.isEmpty()) {
            return new OrderSpecifier[]{pendingChannelMember.requestedAt.desc()};
        }

        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String property = order.getProperty();

                    PathBuilder<?> pathBuilder = new PathBuilder<>(PendingChannelMember.class, "pendingChannelMember");
                    return new OrderSpecifier<>(direction, pathBuilder.getString(property));
                })
                .toArray(OrderSpecifier[]::new);
    }

    @Override
    public void deleteAllByChannelId(Long channelId) {
        queryFactory.delete(bannedChannelMember)
                .where(bannedChannelMember.channel.id.eq(channelId))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }
}
