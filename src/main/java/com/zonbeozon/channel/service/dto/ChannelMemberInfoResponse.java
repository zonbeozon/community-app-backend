package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelRole;

public record ChannelMemberInfoResponse(
        Long memberId,
        String username,
        String profile,
        ChannelRole role
) {
    public static ChannelMemberInfoResponse fromEntity(ChannelMember chMember) {
        return new ChannelMemberInfoResponse(
                chMember.getId(),
                chMember.getMember().getUsername(),
                chMember.getMember().getProfile(),
                chMember.getRole());
    }
}
