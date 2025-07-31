package com.zonbeozon.channel.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.JoinedBlogChannelOverview;
import com.zonbeozon.channel.entity.QChannelMember;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QBlogChannel.blogChannel;
import static com.zonbeozon.image.entity.QImage.image;
import static com.zonbeozon.post.entity.QPostImage.postImage;

@Repository
@RequiredArgsConstructor
public class BlogChannelRepositoryImpl implements BlogChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QChannelMember allChannelMembers = new QChannelMember("allChannelMembers");
    private final QChannelMember requester = new QChannelMember("requester");
    private final QPost latestPost = new QPost("latestPost");

    @Override
    public List<JoinedBlogChannelOverview> getBlogChannelsByMember(Member member, ChannelCreatorType creatorType) {
        return queryFactory.select(Projections.constructor(JoinedBlogChannelOverview.class,
                        requester,
                        blogChannel,
                        allChannelMembers.count(),
                        latestPost
                ))
                .from(blogChannel)
                .join(allChannelMembers).on(
                        allChannelMembers.channel.id.eq(blogChannel.id)
                                .and(allChannelMembers.status.eq(ChannelMemberStatus.ACTIVE))
                )
                .join(requester).on(
                        requester.channel.id.eq(blogChannel.id)
                                .and(requester.member.eq(member))
                ).fetchJoin()
                .leftJoin(latestPost).on(latestPost.id.eq(blogChannel.latestPostId)).fetchJoin()
                .leftJoin(latestPost.author).fetchJoin()
                .leftJoin(latestPost.images, postImage).fetchJoin()
                .leftJoin(postImage.image, image).fetchJoin()
                .where(
                        blogChannel.isDeleted.eq(false)
                                .and(blogChannel.creatorType.eq(creatorType))
                )
                .groupBy(requester, blogChannel, latestPost, postImage, image)
                .orderBy(latestPost.createdAt.desc().nullsLast())
                .fetch();
    }
}
