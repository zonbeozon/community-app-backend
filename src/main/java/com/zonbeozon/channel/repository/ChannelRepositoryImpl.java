package com.zonbeozon.channel.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.ChannelWithMemberCount;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zonbeozon.channel.entity.QChannel.channel;
import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.channel.entity.QChannelProfile.channelProfile;

@Repository
@RequiredArgsConstructor
class ChannelRepositoryImpl implements ChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * todo: 채널타입에 따라 필터링 기능 추가
     */
    @Override
    public Page<ChannelWithMemberCount> searchByKeyword(
            String keyword,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            ChannelType type,
            ChannelContentVisibility visibility,
            ChannelJoinPolicy joinPolicy
    ) {
        OrderSpecifier<?> orderSpecifier = ChannelQuery.getOrderSpecifier(sort, direction);
        long offset = (long) page * size;

        BooleanBuilder whereClause = new BooleanBuilder()
                .and(ChannelQuery.eqContentVisibility(visibility))
                .and(ChannelQuery.eqJoinPolicy(joinPolicy))
                .and(ChannelQuery.containsKeyword(keyword));

        List<ChannelWithMemberCount> channels = queryFactory.select(Projections.constructor(
                    ChannelWithMemberCount.class,
                    channel,
                    channelMember.count()
                ))
                .from(channel)
                .leftJoin(channelMember).on(channelMember.channel.eq(channel).and(ChannelMemberQuery.isActive()))
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
    public Optional<Channel> findChannelByIdWithChannelProfile(Long channelId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(channel)
                .leftJoin(channel.profile, channelProfile).fetchJoin()
                .leftJoin(channelProfile.image).fetchJoin()
                .where(channel.id.eq(channelId))
                .fetchOne()
        );

    }
}
