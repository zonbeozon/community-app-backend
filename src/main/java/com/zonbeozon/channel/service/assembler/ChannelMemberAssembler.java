package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.BannedChannelMemberDto;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.dto.PendingChannelMemberDto;
import com.zonbeozon.channel.repository.BannedChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.PendingChannelMemberRepository;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChannelMemberAssembler {
    private static final Set<String> ALLOWED_ACTIVE_CH_SORT_PROPERTIES = Set.of(
            "createdAt"
    );
    private static final Set<String> ALLOWED_PENDING_CH_SORT_PROPERTIES = Set.of(
            "requestedAt"
    );
    private static final Set<String> ALLOWED_BANNED_CH_SORT_PROPERTIES = Set.of(
            "bannedAt"
    );
    private final ChannelMemberRepository channelMemberRepository;
    private final BannedChannelMemberRepository bannedChannelMemberRepository;
    private final PendingChannelMemberRepository pendingChannelMemberRepository;
    private final ChannelFinder channelFinder;

    public Page<ChannelMemberDto> getPagedActiveChannelMember(Long channelId, Pageable pageable) {
        validateSort(pageable.getSort(), ALLOWED_ACTIVE_CH_SORT_PROPERTIES);
        channelFinder.findByIdElseThrow(channelId);
        return channelMemberRepository.findChannelMemberDtoByChannelId(channelId, pageable);
    }

    public Page<BannedChannelMemberDto> getPagedBannedChannelMember(Long channelId, Pageable pageable) {
        validateSort(pageable.getSort(), ALLOWED_BANNED_CH_SORT_PROPERTIES);
        channelFinder.findByIdElseThrow(channelId);
        return bannedChannelMemberRepository.findByChannelId(channelId, pageable);
    }

    public Page<PendingChannelMemberDto> getPagedPendingChannelMember(Long channelId, Pageable pageable) {
        validateSort(pageable.getSort(), ALLOWED_PENDING_CH_SORT_PROPERTIES);
        channelFinder.findByIdElseThrow(channelId);
        return pendingChannelMemberRepository.findByChannelId(channelId, pageable);
    }

    public List<ChannelMemberDto> getChannelMembers(Long channelId, Collection<Long> memberIds) {
        channelFinder.findByIdElseThrow(channelId);
        List<ChannelMemberDto> channelMembers = channelMemberRepository.findChannelMemberDtoByChannelIdAndMemberIdIn(channelId, memberIds);
        if(channelMembers.size() != memberIds.size())
            throw new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND);
        return channelMembers;
    }

    public ChannelMemberDto getChannelMember(Long channelId, Long memberId) {
        channelFinder.findByIdElseThrow(channelId);
        return channelMemberRepository.findChannelMemberDtoByChannelIdAndMemberId(channelId, memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND));
    }

    private void validateSort(Sort sort, Set<String> sortProperties) {
        for (Sort.Order order : sort) {
            if (!sortProperties.contains(order.getProperty())) {
                throw new UnsupportedOperationException(
                        "해당 Sort기준은 제공하지 않습니다: " + order.getProperty() + "'.허용된 Sort기준은 다음과 같습니다: " + sortProperties
                );
            }
        }
    }
}
