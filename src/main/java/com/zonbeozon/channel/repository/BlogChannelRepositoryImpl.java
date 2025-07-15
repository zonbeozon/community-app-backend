package com.zonbeozon.channel.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.BlogChannelOverview;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QBlogChannel.blogChannel;
import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class BlogChannelRepositoryImpl implements BlogChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BlogChannelOverview> getBlogChannelsByMember(Member member, ChannelCreatorType creatorType) {
        return queryFactory.select(Projections.constructor(BlogChannelOverview.class,
                        blogChannel,
                        channelMember.count(),
                        post
                ))
                .from(blogChannel)
                .join(channelMember).on(
                        channelMember.channel.id.eq(blogChannel.id)
                                .and(ChannelMemberQuery.isActive())
                )
                .leftJoin(post).on(post.id.eq(blogChannel.latestPostId)).fetchJoin()
                .leftJoin(post.author).fetchJoin()
                .where(
                        blogChannel.isDeleted.eq(false)
                                .and(blogChannel.creatorType.eq(creatorType))
                )
                .groupBy(blogChannel, post)
                .orderBy(post.createdAt.desc().nullsLast())
                .fetch();
    }
}
