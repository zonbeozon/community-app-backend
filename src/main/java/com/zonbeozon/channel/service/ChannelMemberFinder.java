package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelMemberFinder {
    private final ChannelMemberRepository channelMemberRepository;

    public ChannelMember findByMemberAndChannelElseThrow(Member member, Channel channel) {
        return channelMemberRepository.findByMemberAndChannel(member, channel)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND));
    }

    public boolean existsByMemberAndChannel(Member member, Channel channel) {
        return channelMemberRepository.existsByMemberAndChannel(member, channel);
    }

    public ChannelMember findByChannelAndMemberIgnoringStatus(Member member, Channel channel) {
        return channelMemberRepository.findByChannelAndMemberIgnoringStatus(member, channel)
                .orElseThrow((() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND)));
    }
}
