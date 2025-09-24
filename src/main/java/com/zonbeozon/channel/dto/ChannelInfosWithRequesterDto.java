package com.zonbeozon.channel.dto;

import java.util.List;

public record ChannelInfosWithRequesterDto(
        List<ChannelInfoWithRequesterDto> channels,
        int totalElements
) {
    public static ChannelInfosWithRequesterDto from(
            List<ChannelInfoWithRequesterDto> channels
    ) {
        return new ChannelInfosWithRequesterDto(channels, channels.size());
    }
}
