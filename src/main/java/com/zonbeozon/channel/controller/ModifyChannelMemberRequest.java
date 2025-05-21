package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.entity.ChannelRole;

public record ModifyChannelMemberRequest(
        ChannelRole role,
        Long targetChannelMemberId
) {
}
