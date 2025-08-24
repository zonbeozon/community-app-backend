package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelMemberFinder {
    private final ChannelMemberRepository channelMemberRepository;

    public ChannelMember findByIdElseThrow(ChannelMemberId id) {
        return channelMemberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND));
    }

    public boolean existsById(ChannelMemberId id) {
        return channelMemberRepository.existsById(id);
    }

    public ChannelMember findByIdIgnoringStatus(ChannelMemberId id) {
        return channelMemberRepository.findByIdIgnoringStatus(id)
                .orElseThrow((() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_NOT_FOUND)));
    }
}
