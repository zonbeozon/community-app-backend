package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepositoryCustom {
    boolean isKicked(ChannelMemberId id);
    List<ChannelMember> findByIdIn(Collection<ChannelMemberId> ids, ChannelMemberFetchOptions options);
    Optional<ChannelMember> findById(ChannelMemberId id, ChannelMemberFetchOptions options);
    Page<ChannelMember> findByChannelIdWithMemberOrderByCreatedAtDesc(Long channelId, ChannelMemberStatus status, Pageable pageable);
}
