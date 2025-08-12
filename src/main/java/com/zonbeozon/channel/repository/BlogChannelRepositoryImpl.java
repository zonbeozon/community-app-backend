package com.zonbeozon.channel.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.JoinedBlogChannelOverview;
import com.zonbeozon.channel.entity.ChannelProfile;
import com.zonbeozon.channel.entity.QChannelMember;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.QMember;
import com.zonbeozon.post.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QBlogChannel.blogChannel;
import static com.zonbeozon.channel.entity.QChannelProfile.channelProfile;
import static com.zonbeozon.image.entity.QImage.image;

@Repository
@RequiredArgsConstructor
public class BlogChannelRepositoryImpl implements BlogChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QChannelMember requester = new QChannelMember("requester");
    private final QPost latestPost = new QPost("latestPost");
    private final QChannelMember latestPostAuthor = new QChannelMember("latestPostAuthor");
    private final QMember authorMember = new QMember("authorMember");

    @Override
    public List<JoinedBlogChannelOverview> getBlogChannelsByMember(Member member, ChannelCreatorType creatorType) {
        return queryFactory.select(Projections.constructor(JoinedBlogChannelOverview.class,
                        requester.role,
                        blogChannel,
                        image,
                        latestPost,
                        latestPostAuthor.role,
                        authorMember
                ))
                .from(blogChannel)
                .join(requester).on(
                        requester.channel.id.eq(blogChannel.id)
                                .and(requester.member.eq(member))
                )
                .leftJoin(blogChannel.profile, channelProfile)
                .leftJoin(channelProfile.image, image)
                .leftJoin(latestPost).on(latestPost.id.eq(blogChannel.latestPostId))
                .leftJoin(latestPostAuthor).on(
                        latestPostAuthor.channel.id.eq(blogChannel.id)
                                .and(latestPostAuthor.member.id.eq(latestPost.author.id)))
                .leftJoin(latestPostAuthor.member, authorMember)
                .where(blogChannel.isDeleted.eq(false)
                        .and(blogChannel.creatorType.eq(creatorType)))
                .groupBy(requester, blogChannel, latestPost, latestPostAuthor, authorMember, image)
                .orderBy(latestPost.createdAt.desc().nullsLast())
                .fetch();
    }
}
