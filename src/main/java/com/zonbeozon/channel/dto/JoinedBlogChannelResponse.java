package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.image.entity.ImageResponse;
import com.zonbeozon.post.dto.PostResponse;

public record JoinedBlogChannelResponse(
    Long channelId,
    ChannelType channelType,
    String title,
    String description,
    ImageResponse profile,
    ChannelSettingResponse settings,
    Long memberCount,
    PostResponse latestPost,
    ChannelMemberResponse requester
) {

    public static JoinedBlogChannelResponse from(JoinedBlogChannelOverview joinedBlogChannelOverview) {
        Channel channel = joinedBlogChannelOverview.getBlogChannel();
        ImageResponse imageResponse = channel.getProfile() == null ? null : ImageResponse.from(channel.getProfile().getImage());
        PostResponse postResponse = joinedBlogChannelOverview.getLatestPost() == null ?
                null : PostResponse.from(
                        joinedBlogChannelOverview.getLatestPost(),
                        joinedBlogChannelOverview.getRequester(),
                        joinedBlogChannelOverview.getLatestPost().getImages());
        return new JoinedBlogChannelResponse(
                channel.getId(),
                channel.getChannelType(),
                channel.getTitle(),
                channel.getDescription(),
                imageResponse,
                ChannelSettingResponse.from(channel.getSetting()),
                joinedBlogChannelOverview.getMemberCount(),
                postResponse,
                ChannelMemberResponse.from(joinedBlogChannelOverview.getRequester())
        );
    }
}
