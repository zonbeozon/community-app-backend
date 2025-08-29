package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.enums.ChannelRole;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zonbeozon.channel.entity.ChannelMember;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, ChannelMemberId>, ChannelMemberRepositoryCustom {
    boolean existsByChannelAndRole(Channel channel, ChannelRole role);
    @Query("SELECT cm FROM ChannelMember cm WHERE cm.id = :id")
    Optional<ChannelMember> findByIdIgnoringStatus(ChannelMemberId id);
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ChannelMember cm WHERE cm.channel.id = :channelId")
    void deleteAllByChannelId(Long channelId);
}
