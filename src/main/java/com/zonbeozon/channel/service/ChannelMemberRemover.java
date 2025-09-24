package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.service.finder.ChannelMemberFinder;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberRemover {
    private final ChannelMemberFinder channelMemberFinder;
    private final ChannelMemberRepository channelMemberRepository;

    public void deleteChannelMember(Long channelId, Long memberId) {
        ChannelMember channelMember = channelMemberFinder.findByChannelIdAndMemberIdElseThrow(channelId, memberId);
        if(!channelMember.canLeaveChannel()) {
            throw new ConflictException(ErrorCode.CHANNEL_LEAVE_NOT_ALLOWED);
        }
        channelMemberRepository.delete(channelMember);
    }
}
