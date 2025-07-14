package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelInviteCode;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.repository.ChannelInviteCodeRepository;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.BadRequestException;
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
    private final ChannelMemberFinder channelMemberFinder;
    private final ChannelInviteCodeRepository channelInviteCodeRepository;
    private final AuthenticationService authenticationService;
    private final ChannelFinder channelFinder;

    /**
     * 채널 생성시 초기 한번만 호출된다.
     */
    public void joinAsOwner(Member requester, Channel channel) {
        if(channelMemberRepository.existsByChannelAndRole(channel, ChannelRole.CHANNEL_OWNER))
            throw new IllegalStateException("중복 owner가 발생했습니다.");
        join(requester, channel, ChannelRole.CHANNEL_OWNER);
    }

    public void joinAsMember(Long channelId) {
        Member requester = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findById(channelId);
        if(channel.getSetting().getJoinPolicy() != ChannelJoinPolicy.OPEN) {
            throw new AccessDeniedException(ErrorCode.CHANNEL_JOIN_DENIED);
        }
        if(channelMemberRepository.isKicked(requester, channel)) {
            throw new AccessDeniedException(ErrorCode.KICKED_MEMBER_CANNOT_JOIN);
        }
        join(requester, channel, ChannelRole.CHANNEL_MEMBER);
    }

    /**
     * kicked 된 맴버도 초대를 통해서는 재가입이 가능하다.
     */
    public void inviteAcceptJoinAsMember(String code) {
        Member requester = authenticationService.getCurrentMember();
        Channel channel = consumeInvite(code, requester);
        if(channel.getSetting().getJoinPolicy() == ChannelJoinPolicy.DENY) {
            throw new AccessDeniedException(ErrorCode.CHANNEL_JOIN_DENIED);
        }
        if(channelMemberRepository.isKicked(requester, channel)) {
            channelMemberFinder.findByChannelAndMemberIgnoringStatus(requester, channel).updateStatusToActive();
            return;
        }
        join(requester, channel, ChannelRole.CHANNEL_MEMBER);
    }

    private void join(Member requester, Channel channel, ChannelRole role) {
        if(channelMemberRepository.existsByMemberAndChannel(requester, channel))
            throw new ConflictException(ErrorCode.ALREADY_JOINED_CHANNEL);
        ChannelMember chMember = ChannelMember.create(requester, channel, role);
        channelMemberRepository.save(chMember);
    }

    private Channel consumeInvite(String code, Member requester) {
        ChannelInviteCode inviteCode = channelInviteCodeRepository.findByCode(code)
                .filter(optCode -> optCode.isApplicable(requester))
                .orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_INVITE_CODE));
        Channel channel = inviteCode.getChannel();
        channelInviteCodeRepository.delete(inviteCode);
        return channel;
    }
}
