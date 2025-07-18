package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.post.dto.PostResponse;

public record JoinedBlogChannelResponse(
    Long channelId,
    ChannelType type,
    String title,
    String description,
    String profile,
    ChannelSettingResponse settings,
    Long memberCount,
    PostResponse latestPost
) {
    public static JoinedBlogChannelResponse from(BlogChannelOverview blogChannelOverview) {
        Channel channel = blogChannelOverview.getBlogChannel();
        PostResponse postResponse = blogChannelOverview.getLatestPost() == null ?
                null : PostResponse.from(blogChannelOverview.getLatestPost());
        return new JoinedBlogChannelResponse(
                channel.getId(),
                channel.getChannelType(),
                channel.getTitle(),
                channel.getDescription(),
                channel.getProfile(),
                ChannelSettingResponse.from(channel.getSetting()),
                blogChannelOverview.getMemberCount(),
                postResponse
        );
    }
}
