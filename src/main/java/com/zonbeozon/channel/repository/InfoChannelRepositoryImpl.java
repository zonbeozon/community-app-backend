package com.zonbeozon.channel.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.InfoChannelOverview;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.channel.entity.QInfoChannel.infoChannel;
import static com.zonbeozon.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class InfoChannelRepositoryImpl implements InfoChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<InfoChannelOverview> getInfoChannelsByMember(Member member, ChannelCreatorType creatorType) {
        return queryFactory.select(Projections.constructor(InfoChannelOverview.class,
                        infoChannel,
                        channelMember.count(),
                        post
                ))
                .from(infoChannel)
                .join(channelMember).on(
                        channelMember.channel.id.eq(infoChannel.id)
                                .and(ChannelMemberQuery.isActive())
                )
                .leftJoin(post).on(post.id.eq(infoChannel.latestPostId)).fetchJoin()
                .leftJoin(post.author).fetchJoin()
                .where(
                        infoChannel.isDeleted.eq(false)
                                .and(infoChannel.creatorType.eq(creatorType))
                )
                .groupBy(infoChannel, post)
                .orderBy(post.createdAt.desc().nullsLast())
                .fetch();
    }
}
