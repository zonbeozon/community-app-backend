package com.zonbeozon.channel.dto;

public record ChannelInfoWithRequesterDto(
    ChannelInfoDto channelInfo,
    ChannelMemberDto requester
) {
}
