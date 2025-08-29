package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.channel.repository.ChannelMemberFetchOptions;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.SortExcludedPageRequest;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChannelMemberAssembler {
    private final ChannelMemberRepository channelMemberRepository;

    /**
     * @throws NotFoundException  주어진 channelMemberIds 개수와 결과 값의 개수가 다를때
     */
    public Map<ChannelMemberId, ChannelMemberResponse> getChannelMemberResponse(Collection<ChannelMemberId> channelMemberIds) {
        List<ChannelMember> channelMembers = channelMemberRepository.findByIdIn(
                channelMemberIds,
                new ChannelMemberFetchOptions.Builder().withMember(true).build());
        if(channelMembers.size() != channelMemberIds.size()) throw new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND);
        Map<ChannelMemberId, ChannelMemberResponse> channelMemberResponseMap = new HashMap<>();
        channelMembers.forEach(channelMember -> channelMemberResponseMap.put(channelMember.getId(), ChannelMemberResponse.from(channelMember)));
        return channelMemberResponseMap;
    }

    public ChannelMemberResponse getChannelMemberResponse(ChannelMemberId channelMemberId) {
        ChannelMember channelMember = channelMemberRepository.findById(channelMemberId, new ChannelMemberFetchOptions.Builder().withMember(true).build())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND));
        return ChannelMemberResponse.from(channelMember);
    }

    public Page<ChannelMemberResponse> getPagedActiveChannelMemberResponse(Long channelId, SortExcludedPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by("createdAt").descending());
        return channelMemberRepository.findByChannelIdWithMemberOrderByCreatedAtDesc(channelId, ChannelMemberStatus.ACTIVE, pageable).map(ChannelMemberResponse::from);
    }

    public Page<ChannelMemberResponse> getPagedBannedChannelMemberResponse(Long channelId, SortExcludedPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by("createdAt").descending());
        return channelMemberRepository.findByChannelIdWithMemberOrderByCreatedAtDesc(channelId, ChannelMemberStatus.BANNED, pageable).map(ChannelMemberResponse::from);
    }

    public Page<ChannelMemberResponse> getPagedPendingChannelMemberResponse(Long channelId, SortExcludedPageRequest pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by("createdAt").descending());
        return channelMemberRepository.findByChannelIdWithMemberOrderByCreatedAtDesc(channelId, ChannelMemberStatus.PENDING, pageable).map(ChannelMemberResponse::from);
    }
}
