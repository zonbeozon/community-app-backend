package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.ChannelMemberCount;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zonbeozon.channel.entity.ChannelMember;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, ChannelMemberId>, ChannelMemberRepositoryCustom {
    boolean existsByChannelAndRole(Channel channel, ChannelRole role);
    @Query("SELECT cm FROM ChannelMember cm WHERE cm.id = :id")
    Optional<ChannelMember> findByIdIgnoringStatus(ChannelMemberId id);
    @Query(
        """
            SELECT new com.zonbeozon.channel.dto.ChannelMemberCount(cm.channel.id, COUNT(cm.id))
            FROM ChannelMember cm
            WHERE cm.channel.id IN :channelIds
            AND cm.status = 'ACTIVE'
            GROUP BY cm.channel.id
       """)
    List<ChannelMemberCount> countChannelMemberByChannelIds(List<Long> channelIds);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ChannelMember cm WHERE cm.channel.id = :channelId")
    void deleteAllByChannelId(Long channelId);
}
