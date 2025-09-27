package com.zonbeozon.channel.dto;

public record ChannelInfoWithMembershipDto(
    ChannelInfoDto channelInfo,
    ChannelMemberDto membership
) {
}
