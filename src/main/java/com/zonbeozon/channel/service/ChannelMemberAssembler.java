package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.channel.security.MemberOfChannelOnly;
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

    @MemberOfChannelOnly
    public Page<ChannelMemberResponse> createPagedActiveChannelMemberResponse(Long channelId, SortExcludedPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by("createdAt").descending());
        return channelMemberRepository.findByChannelId(channelId, ChannelMemberStatus.ACTIVE, pageable).map(ChannelMemberResponse::from);
    }

    @CheckChannelAccess(ChannelAction.READ_KICKED_MEMBER)
    public Page<ChannelMemberResponse> createPagedKickedChannelMemberResponse(Long channelId, SortExcludedPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by("createdAt").descending());
        return channelMemberRepository.findByChannelId(channelId, ChannelMemberStatus.KICKED, pageable).map(ChannelMemberResponse::from);

    }
}
