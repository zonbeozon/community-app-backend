package com.zonbeozon.channel.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.entity.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QChannel.*;
import static com.zonbeozon.channel.entity.QChannelMember.*;

@Repository
@RequiredArgsConstructor
class ChannelRepositoryImpl implements ChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChannelWithMemberCount> searchByKeyword(
            String keyword,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            Channel.Type type,
            Channel.OpenLevel openLevel
    ) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort, direction);
        long offset = (long) page * size;

        List<ChannelWithMemberCount> channels = queryFactory.select(Projections.constructor(
                ChannelWithMemberCount.class
                        ,channel,
                        channelMember.count()
                ))
                .from(channel)
                .where(
                        eqType(type),
                        eqOpenLevel(openLevel),
                        containsKeyword(keyword)
                )
                .leftJoin(channelMember).on(channelMember.channel.id.eq(channel.id))
                .groupBy(channel.id)
                .orderBy(orderSpecifier)
                .offset(offset)
                .limit(size)
                .fetch();

        // total count 조회
        Long total = queryFactory.select(channel.count())
                .from(channel)
                .where(
                        eqType(type),
                        eqOpenLevel(openLevel),
                        containsKeyword(keyword)
                )
                .fetchOne();

        //warning 제거
        long unboxedTotal = total == null ? 0L : total;

        return new PageImpl<>(channels, PageRequest.of(page, size, Sort.by(direction, sort.name())), unboxedTotal);
    }

    private BooleanExpression eqType(Channel.Type type) {
        return type == null ? null : channel.channelType.eq(type);
    }

    private BooleanExpression eqOpenLevel(Channel.OpenLevel openLevel) {
        return openLevel == null ? null : channel.openLevel.eq(openLevel);
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null ? null : channel.title.containsIgnoreCase(keyword); // 대소문자 구분 없이 포함 검색
    }

    // 정렬 조건 반환 메서드
    private OrderSpecifier<?> getOrderSpecifier(ChannelSort sort, Sort.Direction direction) {
        boolean asc = direction.isAscending();

        return switch (sort) {
            case MEMBER_COUNT -> asc ? channelMember.count().asc() : channelMember.count().desc();
            case CREATED_AT -> asc ? channel.createdAt.asc() : channel.createdAt.desc();
        };
    }
}
