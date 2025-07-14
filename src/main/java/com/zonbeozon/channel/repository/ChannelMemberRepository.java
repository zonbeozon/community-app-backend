package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zonbeozon.channel.entity.ChannelMember;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long>, ChannelMemberRepositoryCustom {
    boolean existsByMemberAndChannel(Member member, Channel channel);
    Optional<ChannelMember> findByMemberAndChannel(Member member, Channel channel);
    boolean existsByChannelAndRole(Channel channel, ChannelRole role);
    @Query("SELECT cm FROM ChannelMember cm WHERE cm.member = :member AND cm.channel = :channel")
    Optional<ChannelMember> findByChannelAndMemberIgnoringStatus(Member member, Channel channel);
    List<ChannelMember> findByChannelAndMemberIn(Channel channel, List<Member> members);

}
