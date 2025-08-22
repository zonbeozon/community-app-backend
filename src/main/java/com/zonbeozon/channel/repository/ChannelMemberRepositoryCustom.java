package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChannelMemberRepositoryCustom {
    boolean isKicked(Member member, Channel channel);
    Page<ChannelMember> findByChannelIdWithMemberOrderByCreatedAtDesc(Long channelId, ChannelMemberStatus status, Pageable pageable);
}
