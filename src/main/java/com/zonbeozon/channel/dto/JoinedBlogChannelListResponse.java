package com.zonbeozon.channel.dto;

import java.util.List;


public record JoinedBlogChannelListResponse(
    List<JoinedBlogChannelResponse> channels,
    int totalElements
) {
    public static JoinedBlogChannelListResponse from(List<BlogChannelOverview> channels) {
        return new JoinedBlogChannelListResponse(
                channels.stream().map(JoinedBlogChannelResponse::from).toList(),
                channels.size()
        );
    }
}
