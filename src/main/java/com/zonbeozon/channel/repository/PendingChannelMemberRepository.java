package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.PendingChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PendingChannelMemberRepository extends JpaRepository<PendingChannelMember, Long>, PendingChannelMemberRepositoryCustom {
    boolean existsByChannelIdAndMemberId(Long channelId, Long memberId);
    @Query("""
        SELECT pcm FROM PendingChannelMember pcm
        JOIN FETCH pcm.channel
        JOIN FETCH pcm.member
        WHERE pcm.channel.id = :channelId AND pcm.member.id = :memberId
    """)
    Optional<PendingChannelMember> findByChannelIdAndMemberIdWithChannelAndMember(Long channelId, Long memberId);
    Optional<PendingChannelMember> findByChannelIdAndMemberId(Long channelId, Long memberId);
}
