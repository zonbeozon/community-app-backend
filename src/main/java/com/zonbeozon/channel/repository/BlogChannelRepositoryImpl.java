package com.zonbeozon.channel.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.JoinedBlogChannelOverview;
import com.zonbeozon.channel.entity.BlogChannel;
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
import java.util.Set;

import static com.zonbeozon.channel.entity.QBlogChannel.blogChannel;
import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
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
    public List<BlogChannel> findAllByMemberId(Long memberId) {
        return queryFactory.selectFrom(blogChannel)
                .join(channelMember).on(
                        channelMember.member.id.eq(memberId)
                                .and(channelMember.channel.id.eq(blogChannel.id))
                )
                .fetch();
    }
}
