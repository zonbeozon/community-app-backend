package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zonbeozon.channel.entity.ChannelMember;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
    Optional<ChannelMember> findByMemberAndChannel(Member member, Channel channel);
    Optional<ChannelMember> findByIdAndChannel(Long id, Channel channel);
    int countByChannel(Channel channel);
    List<ChannelMember> findByMember(Member member);
}
