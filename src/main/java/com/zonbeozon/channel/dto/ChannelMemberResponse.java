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
    public static ChannelMemberResponse from(ChannelMember channelMember) {
        return new ChannelMemberResponse(
                channelMember.getMember().getId(),
                channelMember.getMember().getUsername(),
                channelMember.getMember().getProfile(),
                channelMember.getRole());
    }

    public static ChannelMemberResponse from(Member member, ChannelRole role) {
        return new ChannelMemberResponse(
                member.getId(),
                member.getUsername(),
                member.getProfile(),
                role);
    }
}
