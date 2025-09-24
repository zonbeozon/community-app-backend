package com.zonbeozon.channel.dto;

public record JoinRequestDeniedEvent(
        Long channelId,
        Long memberId
) {
}
