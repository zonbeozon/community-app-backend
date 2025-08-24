package com.zonbeozon.channel.entity;

import com.zonbeozon.member.domain.Member;
import jakarta.persistence.Embeddable;

@Embeddable
public record ChannelMemberId(
        Long channelId,
        Long memberId
) {

    public static ChannelMemberId from(Channel channel, Member member) {
        return new ChannelMemberId(channel.getId(), member.getId());
    }
}
