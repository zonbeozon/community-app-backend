package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.PendingChannelMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PendingChannelMemberRepositoryCustom {
    Page<PendingChannelMemberDto> findByChannelId(Long channelId, Pageable pageable);
    void deleteAllByChannelId(Long channelId);
}
