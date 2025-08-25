package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.dto.MemberResponse;

public record ChannelMemberResponse (
        MemberResponse member,
        ChannelRole channelRole
) {
    public static ChannelMemberResponse from(ChannelMember channelMember) {
        return new ChannelMemberResponse(
                MemberResponse.from(channelMember.getMember()),
                channelMember.getRole());
    }

    public static ChannelMemberResponse from(Member member, ChannelRole role) {
        return new ChannelMemberResponse(
                MemberResponse.from(member),
                role
        );
    }
}
