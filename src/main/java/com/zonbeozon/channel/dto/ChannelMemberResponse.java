package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.member.domain.Member;

public record ChannelMemberResponse (
        Long memberId,
        String username,
        String profile,
        ChannelRole role
) {
    public static ChannelMemberResponse from(ChannelMember chMember) {
        return new ChannelMemberResponse(
                chMember.getMember().getId(),
                chMember.getMember().getUsername(),
                chMember.getMember().getProfile(),
                chMember.getRole());
    }

    public static ChannelMemberResponse from(Member member, ChannelRole role) {
        return new ChannelMemberResponse(
                member.getId(),
                member.getUsername(),
                member.getProfile(),
                role);
    }
}
