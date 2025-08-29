package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberRemover {
    private final ChannelFinder channelFinder;
    private final ChannelMemberFinder channelMemberFinder;
    private final ChannelMemberRepository channelMemberRepository;
    private final AuthenticationService authenticationService;

    public void leaveChannel(Long channelId) {
        Member member = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        ChannelMember channelMember = channelMemberFinder.findByIdElseThrow(ChannelMemberId.from(channel, member));

        if(!channelMember.canLeaveChannel()) {
            throw new ConflictException(ErrorCode.CHANNEL_LEAVE_NOT_ALLOWED);
        }
        channelMemberRepository.delete(channelMember);
    }
}
