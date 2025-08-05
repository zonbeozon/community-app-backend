package com.zonbeozon.channel.dto;

import java.util.List;

public record JoinedBlogChannelListResponse(
        List<JoinedBlogChannelResponse> channels,
        int totalElements
) {
    public static JoinedBlogChannelListResponse from(
            List<JoinedBlogChannelResponse> channels
    ) {
        return new JoinedBlogChannelListResponse(channels, channels.size());
    }
}
