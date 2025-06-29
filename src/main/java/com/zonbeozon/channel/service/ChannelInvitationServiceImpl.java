package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAccessDeniedException;
import com.zonbeozon.channel.exception.ChannelBadRequestException;
import com.zonbeozon.channel.repository.ChannelInviteCodeRepository;
import com.zonbeozon.channel.service.dto.InviteCodeResponse;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ChannelInvitationServiceImpl {
    private final MemberService memberService;
    private final ChannelInviteCodeRepository channelInviteCodeRepository;
    private final ChannelMemberServiceImpl channelMemberServiceImpl;
    private final ChannelServiceImpl channelServiceImpl;

    public InviteCodeResponse publishInvite(ChannelMember inviter, Long inviteeId) {
        inviter.getChannel().validateInvitePermission(inviter);
        ChannelInviteCode inviteCode = ChannelInviteCode.generate(inviter, inviteeId);
        channelInviteCodeRepository.save(inviteCode);

        return InviteCodeResponse.with(
                inviteCode.getCode(),
                inviter.getChannel(),
                memberService.createMemberResponse(inviter.getMember())
        );
    }

    public void inviteAcceptJoinAsMember(Member invitee, String code) {
        Channel channel = consumeInvite(code, invitee);
        channelMemberServiceImpl.inviteAcceptJoinAsMember(invitee, channel);
    }

    private Channel consumeInvite(String code, Member requester) {
        ChannelInviteCode inviteCode = channelInviteCodeRepository.findByCode(code)
                .filter(optCode -> optCode.isApplicable(requester))
                .orElseThrow(() -> new ChannelBadRequestException(ChannelBadRequestException.ErrorCode.INVITATION_CODE_INVALID));
        Channel channel = inviteCode.getChannel();
        channelInviteCodeRepository.delete(inviteCode);
        return channel;
    }

}
