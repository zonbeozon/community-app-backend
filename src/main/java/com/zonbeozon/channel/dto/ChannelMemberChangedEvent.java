package com.zonbeozon.channel.dto;

public record ChannelMemberChangedEvent(
        Long channelId,
        int delta
) {
}
