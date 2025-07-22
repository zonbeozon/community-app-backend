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
    PostResponse latestPost
) {

    public static JoinedBlogChannelResponse from(BlogChannelOverview blogChannelOverview) {
        Channel channel = blogChannelOverview.getBlogChannel();
        ImageResponse imageResponse = channel.getProfile() == null ? null : ImageResponse.from(channel.getProfile().getImage());
        PostResponse postResponse = blogChannelOverview.getLatestPost() == null ?
                null : PostResponse.from(blogChannelOverview.getLatestPost());
        return new JoinedBlogChannelResponse(
                channel.getId(),
                channel.getChannelType(),
                channel.getTitle(),
                channel.getDescription(),
                imageResponse,
                ChannelSettingResponse.from(channel.getSetting()),
                blogChannelOverview.getMemberCount(),
                postResponse
        );
    }
}
