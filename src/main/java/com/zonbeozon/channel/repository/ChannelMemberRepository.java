package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.ChannelMemberCount;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zonbeozon.channel.entity.ChannelMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long>, ChannelMemberRepositoryCustom {
    boolean existsByMemberAndChannel(Member member, Channel channel);
    Optional<ChannelMember> findByMemberAndChannel(Member member, Channel channel);
    boolean existsByChannelAndRole(Channel channel, ChannelRole role);
    @Query("SELECT cm FROM ChannelMember cm WHERE cm.member = :member AND cm.channel = :channel")
    Optional<ChannelMember> findByChannelAndMemberIgnoringStatus(Member member, Channel channel);
    @EntityGraph(attributePaths = {"member"})
    List<ChannelMember> findByChannelAndMemberIn(Channel channel, List<Member> members);
    @Query(
        """
            SELECT new com.zonbeozon.channel.dto.ChannelMemberCount(cm.channel.id, COUNT(cm.id))
            FROM ChannelMember cm
            WHERE cm.channel.id IN :channelIds
            AND cm.status = 'ACTIVE'
            GROUP BY cm.channel.id
       """)
    List<ChannelMemberCount> countChannelMemberByChannelIds(List<Long> channelIds);

}
