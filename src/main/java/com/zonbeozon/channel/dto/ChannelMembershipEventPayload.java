package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelMembershipEventType;

public record ChannelMembershipEventPayload (
        ChannelMembershipEventType type,
        Long channelId
) {
}
