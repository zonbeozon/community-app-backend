package com.zonbeozon.channel.dto;

import java.util.List;

public record ChannelInfosWithRequesterResponse(
        List<ChannelInfoWithRequesterResponse> channels,
        int totalElements
) {
    public static ChannelInfosWithRequesterResponse from(
            List<ChannelInfoWithRequesterResponse> channels
    ) {
        return new ChannelInfosWithRequesterResponse(channels, channels.size());
    }
}
