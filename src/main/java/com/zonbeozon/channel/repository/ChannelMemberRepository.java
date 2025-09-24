package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelRole;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zonbeozon.channel.entity.ChannelMember;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long>, ChannelMemberRepositoryCustom {
    boolean existsByChannelAndRole(Channel channel, ChannelRole role);

    @Query("""
        SELECT cm FROM ChannelMember cm
        JOIN FETCH cm.channel
        JOIN FETCH cm.member
        WHERE cm.channel.id = :channelId AND cm.member.id = :memberId
    """)
    Optional<ChannelMember> findByChannelIdAndMemberIdWithChannelAndMember(Long channelId, Long memberId);

    Optional<ChannelMember> findByChannelIdAndMemberId(Long channelId, Long memberId);
    boolean existsByChannelIdAndMemberId(Long channelId, Long memberId);
}
