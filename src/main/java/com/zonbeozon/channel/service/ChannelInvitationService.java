package com.zonbeozon.channel.service;

import com.zonbeozon.channel.ChannelContext;
import com.zonbeozon.channel.ChannelPermissionValidator;
import com.zonbeozon.channel.UseChannelContext;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAccessDeniedException;
import com.zonbeozon.channel.repository.ChannelInviteCodeRepository;
import com.zonbeozon.channel.service.dto.InviteCodeResponse;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@UseChannelContext
public class ChannelInvitationService {
    private final MemberService memberService;
    private final ChannelInviteCodeRepository channelInviteCodeRepository;
    private final ChannelPermissionValidator permissionValidator;
    private final ChannelMemberService channelMemberService;
    private final ChannelService channelService;

    public InviteCodeResponse publishInvite(ChannelContext channelContext, Long inviteeId) {
        permissionValidator.validate(ChannelAction.INVITE, channelContext);
        ChannelInviteCode inviteCode = ChannelInviteCode.generate(channelContext.getChannelMember(), inviteeId);
        channelInviteCodeRepository.save(inviteCode);

        return new InviteCodeResponse(
                inviteCode.getCode(),
                channelService.getChannelResponse(channelContext.getChannel()),
                memberService.createMemberResponse(channelContext.getMember())
        );
    }

    public void inviteAcceptJoinAsMember(ChannelContext channelContext, Member requester, String code) {
        consumeInvite(channelContext.getChannel(), code, requester);
        channelMemberService.subscribeChannel(requester, channelContext.getChannel(), ChannelRole.CHANNEL_MEMBER);
    }

    public void consumeInvite(Channel channel, String code, Member requester) {
        ChannelInviteCode inviteCode = channelInviteCodeRepository.findByCodeAndChannel(code, channel)
                .filter(optCode -> optCode.isApplicable(requester))
                .orElseThrow(() -> new ChannelAccessDeniedException("기한이 만료되었거나 잘못된 초대 코드 입니다."));
        channelInviteCodeRepository.delete(inviteCode);
    }

}
