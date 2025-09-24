package com.zonbeozon.channel.dto;

public record ChannelMemberBannedEvent(
        Long channelId,
        Long memberId
) {
}
