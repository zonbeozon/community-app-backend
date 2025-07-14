package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelInviteCode;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.repository.ChannelInviteCodeRepository;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelInviteCodeCreator {
    private final ChannelInviteCodeRepository channelInviteCodeRepository;
    private final ChannelFinder channelFinder;
    private final AuthenticationService authenticationService;

    /**
     * todo: sender 호출해서 stomp로 메시지 보내기.
     */
    @CheckChannelAccess(ChannelAction.INVITE_PUBLISH)
    public void publishInvite(Long channelId, Long inviteeId) {
        Channel channel = channelFinder.findById(channelId);
        if(channel.getSetting().getJoinPolicy() == ChannelJoinPolicy.DENY) {
            throw new ConflictException(ErrorCode.INVITE_GENERATE_DENIED);
        }
        Member requester = authenticationService.getCurrentMember();
        ChannelInviteCode inviteCode = ChannelInviteCode.generate(channel, requester, inviteeId);
        channelInviteCodeRepository.save(inviteCode);
    }
}
