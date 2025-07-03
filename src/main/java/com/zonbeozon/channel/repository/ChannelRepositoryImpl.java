package com.zonbeozon.channel.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QChannel.*;
import static com.zonbeozon.channel.entity.QChannelMember.*;
import static com.zonbeozon.post.entity.QPost.*;

@Repository
@RequiredArgsConstructor
class ChannelRepositoryImpl implements ChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private BooleanBuilder createDefaultBooleanBuilder() {
        return new BooleanBuilder()
                .and(channel.isDeleted.eq(false));
    }

    @Override
    public Page<ChannelWithMemberCount> searchByKeyword(
            String keyword,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            ChannelType type,
            ChannelContentOpenLevel contentOpenLevel,
            ChannelJoinLevel joinLevel
    ) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort, direction);
        long offset = (long) page * size;

        BooleanBuilder whereClause = createDefaultBooleanBuilder()
                .and(eqType(type))
                .and(eqOpenLevel(contentOpenLevel))
                .and(eqJoinLevel(joinLevel))
                .and(containsKeyword(keyword));

        List<ChannelWithMemberCount> channels = queryFactory.select(Projections.constructor(
                ChannelWithMemberCount.class
                        ,channel,
                        channelMember.count()
                ))
                .from(channel)
                .leftJoin(channelMember).on(channelMember.channel.id.eq(channel.id))
                .where(whereClause)
                .groupBy(channel.id)
                .orderBy(orderSpecifier)
                .offset(offset)
                .limit(size)
                .fetch();

        // total count 조회
        Long total = queryFactory.select(channel.count())
                .from(channel)
                .where(whereClause)
                .fetchOne();

        //warning 제거
        long unboxedTotal = total == null ? 0L : total;

        return new PageImpl<>(channels, PageRequest.of(page, size, Sort.by(direction, sort.name())), unboxedTotal);
    }

    @Override
    public List<JoinedChannelDto> findJoinedChannels(Member member) {
        BooleanBuilder whereClause = createDefaultBooleanBuilder()
                .and(channelMember.member.eq(member));

        return queryFactory.select(Projections.constructor(
                JoinedChannelDto.class,
                        channel,
                        channelMember.count(),
                        post.content,
                        post.createdAt
                ))
                .from(channelMember)
                .innerJoin(channelMember.channel, channel)
                .leftJoin(post).on(
                        post.channel.eq(channel),
                        post.createdAt.eq(
                                JPAExpressions.select(post.createdAt.max())
                                        .from(post)
                                        .where(post.channel.eq(channel))
                        )
                )
                .where(whereClause)
                .groupBy(channel.id, post.id)
                .orderBy(getOrderSpecifier(ChannelSort.LATEST_POST_CREATED_AT, Sort.Direction.DESC))
                .fetch();
    }

    private BooleanExpression eqType(ChannelType type) {
        return type == null ? null : channel.type.eq(type);
    }

    private BooleanExpression eqOpenLevel(ChannelContentOpenLevel contentOpenLevel) {
        return contentOpenLevel == null ? null : channel.contentOpenLevel.eq(contentOpenLevel);
    }

    private BooleanExpression eqJoinLevel(ChannelJoinLevel joinLevel) {
        return joinLevel == null ? null : channel.joinLevel.eq(joinLevel);
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null ? null : channel.title.containsIgnoreCase(keyword); // 대소문자 구분 없이 포함 검색
    }

    private OrderSpecifier<?> getOrderSpecifier(ChannelSort sort, Sort.Direction direction) {
        boolean asc = direction.isAscending();

        return switch (sort) {
            case MEMBER_COUNT -> asc ? channelMember.count().asc() : channelMember.count().desc();
            case CHANNEL_CREATED_AT -> asc ? channel.createdAt.asc() : channel.createdAt.desc();
            case LATEST_POST_CREATED_AT -> new OrderSpecifier<>(
                    asc ? Order.ASC : Order.DESC,
                    JPAExpressions
                            .select(post.createdAt.max())
                            .from(post)
                            .where(post.channel.eq(channel))
            );
        };
    }
}
