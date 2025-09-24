package com.zonbeozon.channel.service.finder;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelMemberFinder {
    private final ChannelMemberRepository channelMemberRepository;

    public ChannelMember findByChannelIdAndMemberIdElseThrow(Long channelId, Long memberId) {
        return channelMemberRepository.findByChannelIdAndMemberId(channelId, memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND));
    }

    public Optional<ChannelMember> findByChannelIdAndMemberId(Long channelId, Long memberId) {
        return channelMemberRepository.findByChannelIdAndMemberId(channelId, memberId);
    }

    public ChannelMember findByChannelIdAndMemberIdWithChannelAndMemberElseThrow(Long channelId, Long memberId) {
        return channelMemberRepository.findByChannelIdAndMemberIdWithChannelAndMember(channelId, memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND));
    }

    public boolean existsByChannelIdAndMemberId(Long channelId, Long memberId) {
        return channelMemberRepository.existsByChannelIdAndMemberId(channelId, memberId);
    }
}
