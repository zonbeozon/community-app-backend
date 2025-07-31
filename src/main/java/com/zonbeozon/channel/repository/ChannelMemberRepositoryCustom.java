package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChannelMemberRepositoryCustom {
    boolean isKicked(Member member, Channel channel);
    Page<ChannelMember> findByChannelId(Long channelId, ChannelMemberStatus status, Pageable pageable);
}
