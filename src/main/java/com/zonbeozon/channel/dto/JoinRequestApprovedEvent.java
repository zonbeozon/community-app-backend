package com.zonbeozon.channel.dto;

public record JoinRequestApprovedEvent(
        Long channelId,
        Long memberId
) {
}
