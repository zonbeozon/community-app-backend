package com.zonbeozon.channel.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.ChannelWithMemberCount;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zonbeozon.channel.entity.QBlogChannel.blogChannel;
import static com.zonbeozon.channel.entity.QChannel.channel;
import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.channel.entity.QChannelProfile.channelProfile;

@Repository
@RequiredArgsConstructor
class ChannelRepositoryImpl implements ChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

//    @Override
//    public Page<ChannelWithMemberCount> searchByKeyword(
//            String keyword,
//            int page,
//            int size,
//            ChannelSort sort,
//            Sort.Direction direction,
//            ChannelType type,
//            ChannelContentVisibility visibility,
//            ChannelJoinPolicy joinPolicy
//    ) {
//        OrderSpecifier<?> orderSpecifier = ChannelQuery.getOrderSpecifier(sort, direction);
//        long offset = (long) page * size;
//
//        BooleanBuilder whereClause = new BooleanBuilder()
//                .and(ChannelQuery.eqContentVisibility(visibility))
//                .and(ChannelQuery.eqJoinPolicy(joinPolicy))
//                .and(ChannelQuery.containsKeyword(keyword));
//
//        List<ChannelWithMemberCount> channels = queryFactory.select(Projections.constructor(
//                    ChannelWithMemberCount.class,
//                    channel,
//                    channelMember.count()
//                ))
//                .from(channel)
//                .leftJoin(channelMember).on(channelMember.channel.eq(channel).and(ChannelMemberQuery.isActive()))
//                .where(whereClause)
//                .groupBy(channel.id)
//                .orderBy(orderSpecifier)
//                .offset(offset)
//                .limit(size)
//                .fetch();
//
//        // total count 조회
//        Long total = queryFactory.select(channel.count())
//                .from(channel)
//                .where(whereClause)
//                .fetchOne();
//
//        //warning 제거
//        long unboxedTotal = total == null ? 0L : total;
//
//        return new PageImpl<>(channels, PageRequest.of(page, size, Sort.by(direction, sort.name())), unboxedTotal);
//    }

    @Override
    public Optional<Channel> findByIdWithProfile(Long channelId) {
        JPAQuery<Channel> query = queryFactory.selectFrom(channel);
        leftJoinProfileAndImage(query);
        return Optional.ofNullable(
                query.where(channel.id.eq(channelId)).fetchOne()
        );
    }

    @Override
    public Optional<ChannelWithMemberCount> findByIdWithProfileAndMemberCount(Long channelId) {
        return Optional.ofNullable(buildChannelWithMemberCountQuery(channel.id.eq(channelId)).fetchOne());
    }

    @Override
    public List<ChannelWithMemberCount> findByIdInWithProfileAndMemberCount(List<Long> channelIds) {
        return buildChannelWithMemberCountQuery(channel.id.in(channelIds)).fetch();
    }

    @Override
    public List<Channel> findAllByMemberIdOrderByLatestEventOccurred(Long memberId) {
        return queryFactory.selectFrom(channel)
                .join(channelMember).on(channelMember.member.id.eq(memberId))
                .where(channelMember.channel.eq(channel))
                .orderBy(channel.latestEventOccurred.desc().nullsLast())
                .fetch();
    }

    private JPAQuery<ChannelWithMemberCount> buildChannelWithMemberCountQuery(Predicate whereClause) {
        JPAQuery<ChannelWithMemberCount> query = queryFactory
                .select(Projections.constructor(ChannelWithMemberCount.class,
                        channel,
                        channelMember.count()
                )).from(channel);

        leftJoinProfileAndImage(query);
        return query
                .leftJoin(channel.channelMembers, channelMember)
                .where(whereClause)
                .groupBy(channel);
    }

    private <T> JPAQuery<T> leftJoinProfileAndImage(JPAQuery<T> query) {
        return query
                .leftJoin(channel.profile, channelProfile).fetchJoin()
                .leftJoin(channelProfile.image).fetchJoin();
    }
}
