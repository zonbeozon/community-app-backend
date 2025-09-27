package com.zonbeozon.channel.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.ChannelInfoWithMembershipDto;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.repository.expression.ChannelConstructorExpression;
import com.zonbeozon.image.entity.QImage;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.zonbeozon.channel.entity.QChannel.channel;
import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.channel.entity.QChannelProfile.*;
import static com.zonbeozon.member.domain.QMember.member;
import static com.zonbeozon.member.domain.QMemberProfile.memberProfile;


@Slf4j
@RequiredArgsConstructor
@Repository
public class ChannelMemberRepositoryImpl implements ChannelMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    private static final QImage memberProfileImage = new QImage("memberProfileImage");
    private static final QImage channelProfileImage = new QImage("channelProfileImage");


    @Override
    public List<ChannelInfoWithMembershipDto> findChannelInfoWithRequesterByMemberIdOrderByLatestEventOccurredDesc(Long memberId) {
        JPAQuery<ChannelInfoWithMembershipDto> query = queryFactory.select(
                ChannelConstructorExpression.channelInfoWithMembership(
                        channel,
                        channelProfile,
                        channelProfileImage,
                        member,
                        channelMember,
                        memberProfile,
                        memberProfileImage
                )).from(channelMember);

        query.join(channelMember.channel, channel)
                .leftJoin(channel.profile, channelProfile)
                .leftJoin(channelProfile.image, channelProfileImage)
                .join(channelMember.member, member)
                .leftJoin(member.profile, memberProfile)
                .leftJoin(memberProfile.image, memberProfileImage);

        query.where(channelMember.member.id.eq(memberId));
        query.orderBy(channel.latestEventOccurred.desc());
        return query.fetch();
    }

    @Override
    public Optional<ChannelInfoWithMembershipDto> findChannelInfoWithRequesterByChannelIdAndMemberId(Long channelId, Long memberId) {
        JPAQuery<ChannelInfoWithMembershipDto> query = queryFactory.select(
                ChannelConstructorExpression.channelInfoWithMembership(
                        channel,
                        channelProfile,
                        channelProfileImage,
                        member,
                        channelMember,
                        memberProfile,
                        memberProfileImage
                )).from(channelMember);

        query.join(channelMember.channel, channel)
                .leftJoin(channel.profile, channelProfile)
                .leftJoin(channelProfile.image, channelProfileImage)
                .join(channelMember.member, member)
                .leftJoin(member.profile, memberProfile)
                .leftJoin(memberProfile.image, memberProfileImage);

        query.where(channelMember.member.id.eq(memberId).and(channel.id.eq(channelId)));

        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Page<ChannelMemberDto> findChannelMemberDtoByChannelId(Long channelId, Pageable pageable) {
        JPAQuery<ChannelMemberDto> query = queryFactory
                .select(ChannelConstructorExpression.channelMemberDto(
                        member, channelMember, memberProfile, memberProfileImage
                ))
                .from(channelMember);

        query.join(channelMember.member, member)
                .leftJoin(member.profile, memberProfile)
                .leftJoin(memberProfile.image, memberProfileImage);


        query.where(channelMember.channel.id.eq(channelId));

        query.orderBy(getOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
                .select(channelMember.count())
                .from(channelMember)
                .where(channelMember.channel.id.eq(channelId));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchOne);
    }

    @Override
    public List<ChannelMemberDto> findChannelMemberDtoByChannelIdAndMemberIdIn(Long channelId, Collection<Long> memberIds) {
        JPAQuery<ChannelMemberDto> query = queryFactory
                .select(ChannelConstructorExpression.channelMemberDto(
                        member, channelMember, memberProfile, memberProfileImage
                ))
                .from(channelMember);

        query.join(channelMember.member, member)
                .leftJoin(member.profile, memberProfile)
                .leftJoin(memberProfile.image, memberProfileImage);

        query.where(channelMember.channel.id.eq(channelId).and(member.id.in(memberIds)));

        return query.fetch();
    }

    @Override
    public void deleteAllByChannelId(Long channelId) {
        queryFactory.delete(channelMember)
                .where(channelMember.channel.id.eq(channelId))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public Optional<ChannelMemberDto> findChannelMemberDtoByChannelIdAndMemberId(Long channelId, Long memberId) {
        JPAQuery<ChannelMemberDto> query = queryFactory
                .select(ChannelConstructorExpression.channelMemberDto(
                        member, channelMember, memberProfile, memberProfileImage
                ))
                .from(channelMember);


        query.join(channelMember.member, member)
                .leftJoin(member.profile, memberProfile)
                .leftJoin(memberProfile.image, memberProfileImage);

        query.where(channelMember.channel.id.eq(channelId).and(member.id.eq(memberId)));

        return Optional.ofNullable(query.fetchOne());
    }


    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        if (sort.isEmpty()) {
            return new OrderSpecifier[]{channelMember.createdAt.desc()};
        }

        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String property = order.getProperty();

                    PathBuilder<?> pathBuilder = new PathBuilder<>(ChannelMember.class, "channelMember");
                    return new OrderSpecifier<>(direction, pathBuilder.getString(property));
                })
                .toArray(OrderSpecifier[]::new);
    }
}
