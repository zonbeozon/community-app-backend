package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelEventType;

public record ChannelEventResponse(
        ChannelEventType type
) {
}
