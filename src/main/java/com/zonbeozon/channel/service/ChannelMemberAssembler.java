package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.SortExcludedPageRequest;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChannelMemberAssembler {
    private final ChannelMemberRepository channelMemberRepository;

    public List<ChannelMemberResponse> createChannelMemberListResponse(List<Member> members, Channel channel) {
        return channelMemberRepository.findByChannelAndMemberIn(channel, members).stream()
                .map(ChannelMemberResponse::from)
                .toList();
    }

    public Page<ChannelMemberResponse> createPagedActiveChannelMemberResponse(Long channelId, SortExcludedPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by("createdAt").descending());
        return channelMemberRepository.findByChannelIdWithMemberOrderByCreatedAtDesc(channelId, ChannelMemberStatus.ACTIVE, pageable).map(ChannelMemberResponse::from);
    }

    public Page<ChannelMemberResponse> createPagedKickedChannelMemberResponse(Long channelId, SortExcludedPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by("createdAt").descending());
        return channelMemberRepository.findByChannelIdWithMemberOrderByCreatedAtDesc(channelId, ChannelMemberStatus.KICKED, pageable).map(ChannelMemberResponse::from);

    }
}
