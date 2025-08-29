package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelMemberEventType;

public record ChannelMemberEventResponse(
        ChannelMemberEventType type,
        Long channelId
) {
}
