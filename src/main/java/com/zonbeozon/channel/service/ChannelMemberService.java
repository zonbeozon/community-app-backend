package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelRole;
import com.zonbeozon.member.domain.Member;

public interface ChannelMemberService {
    void joinAsMember(Member member, Long channelId);
    void kickMember(Member member, Long channelId, Long targetChannelMemberId);
    void modifyChannelMemberRole(Member member, Long channelId, Long targetChannelMemberId, ChannelRole wantToChange);
    void leaveChannel(Member member, Long channelId);
}
