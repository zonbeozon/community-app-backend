package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;

public interface ChannelMemberRepositoryCustom {
    boolean isKicked(Member member, Channel channel);
}
