package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.BannedChannelMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BannedChannelMemberRepositoryCustom {
    Page<BannedChannelMemberDto> findByChannelId(Long channelId, Pageable pageable);
    void deleteAllByChannelId(Long channelId);
}
