package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.ChannelMemberId;

public record JoinRequestApprovedEvent(
        ChannelMemberId channelMemberId
) {
}
