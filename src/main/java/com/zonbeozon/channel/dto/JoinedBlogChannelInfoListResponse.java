package com.zonbeozon.channel.dto;

import java.util.List;

public record JoinedBlogChannelInfoListResponse(
        List<JoinedBlogChannelInfoResponse> channels,
        int totalElements
) {
    public static JoinedBlogChannelInfoListResponse from(
            List<JoinedBlogChannelInfoResponse> channels
    ) {
        return new JoinedBlogChannelInfoListResponse(channels, channels.size());
    }
}
