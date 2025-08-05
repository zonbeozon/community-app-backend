package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.entity.ImageResponse;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;

public record JoinedBlogChannelResponse(
    Long channelId,
    ChannelType channelType,
    String title,
    String description,
    ImageResponse profile,
    ChannelSettingResponse settings,
    Long memberCount,
    LatestPostResponse latestPost,
    ChannelMemberResponse requester
) {
    public static JoinedBlogChannelResponse from(
            Member requester,
            ChannelRole requesterRole,
            BlogChannel channel,
            Image profile,
            Long memberCount,
            Post latestPost,
            ChannelRole latestPostAuthorRole,
            Member latestPostAuthor,
            Long postImageCount
    ) {
        ImageResponse imageResponse = profile == null ? null : ImageResponse.from(profile);
        LatestPostResponse latestPostResponse = latestPost == null ? null : LatestPostResponse.from(latestPostAuthorRole, latestPostAuthor, latestPost, postImageCount);
        return new JoinedBlogChannelResponse(
                channel.getId(),
                channel.getChannelType(),
                channel.getTitle(),
                channel.getDescription(),
                imageResponse,
                ChannelSettingResponse.from(channel.getSetting()),
                memberCount,
                latestPostResponse,
                ChannelMemberResponse.from(requester, requesterRole)
        );
    }
}
