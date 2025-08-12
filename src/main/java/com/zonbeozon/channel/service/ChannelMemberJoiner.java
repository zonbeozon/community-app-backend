package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.JoinResultStatus;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberJoiner {
    private final ChannelMemberRepository channelMemberRepository;
    private final AuthenticationService authenticationService;
    private final ChannelFinder channelFinder;

    /**
     * 채널 생성시 초기 한번만 호출된다.
     */
    public void joinAsOwner(Member requester, Channel channel) {
        if(channelMemberRepository.existsByChannelAndRole(channel, ChannelRole.CHANNEL_OWNER))
            throw new IllegalStateException("중복 owner가 발생했습니다.");
        join(requester, channel, ChannelRole.CHANNEL_OWNER, ChannelMemberStatus.ACTIVE);
    }

    public JoinResultStatus joinAsMember(Long channelId) {
        Member requester = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        if(channel.getSetting().getJoinPolicy() == ChannelJoinPolicy.DENY) {
            throw new AccessDeniedException(ErrorCode.CHANNEL_JOIN_DENIED);
        }

        if(channelMemberRepository.isKicked(requester, channel)) {
            throw new AccessDeniedException(ErrorCode.KICKED_MEMBER_CANNOT_JOIN);
        }

        if(channel.getSetting().getJoinPolicy() == ChannelJoinPolicy.APPROVAL) {
            join(requester, channel, ChannelRole.CHANNEL_MEMBER, ChannelMemberStatus.PENDING);
            return JoinResultStatus.APPROVAL_REQUESTED;
        }
        join(requester, channel, ChannelRole.CHANNEL_MEMBER, ChannelMemberStatus.ACTIVE);
        return JoinResultStatus.JOINED_IMMEDIATELY;
    }

    private void join(Member requester, Channel channel, ChannelRole role, ChannelMemberStatus status) {
        if(channelMemberRepository.existsByMemberAndChannel(requester, channel))
            throw new ConflictException(ErrorCode.ALREADY_JOINED_CHANNEL);
        ChannelMember chMember = ChannelMember.create(requester, channel, role, status);
        channelMemberRepository.save(chMember);
    }
}
