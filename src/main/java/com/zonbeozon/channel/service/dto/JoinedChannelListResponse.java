package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.repository.JoinedChannelDto;

import java.util.List;

public record JoinedChannelListResponse(
    List<JoinedChannelResponse> channels,
    int totalElements
) {
    public static JoinedChannelListResponse from(List<JoinedChannelDto> joinedChannels) {
        return new JoinedChannelListResponse(
                joinedChannels.stream().map(JoinedChannelResponse::from).toList(),
                joinedChannels.size()
        );
    }
}
