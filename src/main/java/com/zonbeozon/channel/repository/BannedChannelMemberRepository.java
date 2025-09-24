package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.BannedChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannedChannelMemberRepository extends JpaRepository<BannedChannelMember, Long>, BannedChannelMemberRepositoryCustom {
    Optional<BannedChannelMember> findByChannelIdAndMemberId(Long channelId, Long memberId);
    boolean existsByChannelIdAndMemberId(Long channelId, Long memberId);

}
