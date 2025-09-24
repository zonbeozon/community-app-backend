package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.ChannelInfoWithRequesterDto;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepositoryCustom {
    List<ChannelInfoWithRequesterDto> findChannelInfoWithRequesterByMemberIdOrderByLatestEventOccurredDesc(Long memberId);
    Optional<ChannelInfoWithRequesterDto> findChannelInfoWithRequesterByChannelIdAndMemberId(Long channelId, Long memberId);
    Page<ChannelMemberDto> findChannelMemberDtoByChannelId(Long channelId, Pageable pageable);
    List<ChannelMemberDto> findChannelMemberDtoByChannelIdAndMemberIdIn(Long channelId, Collection<Long> memberIds);
    void deleteAllByChannelId(Long channelId);
    Optional<ChannelMemberDto> findChannelMemberDtoByChannelIdAndMemberId(Long channelId, Long memberId);

}
