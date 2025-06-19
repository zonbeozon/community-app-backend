package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberEntityQueryService {
    ChannelMember getChannelMemberOrThrow(Member member, Channel channel);
    Optional<ChannelMember> getChannelMember(Member member, Channel channel);
    List<ChannelMember> getChannelMembersByMember(Member member);
    ChannelMember getByIdOrThrow(Long id);
}
