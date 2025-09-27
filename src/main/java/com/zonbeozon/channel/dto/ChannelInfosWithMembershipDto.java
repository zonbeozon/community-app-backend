package com.zonbeozon.channel.dto;

import java.util.List;

public record ChannelInfosWithMembershipDto(
        List<ChannelInfoWithMembershipDto> channels,
        int totalElements
) {
    public static ChannelInfosWithMembershipDto from(
            List<ChannelInfoWithMembershipDto> channels
    ) {
        return new ChannelInfosWithMembershipDto(channels, channels.size());
    }
}
