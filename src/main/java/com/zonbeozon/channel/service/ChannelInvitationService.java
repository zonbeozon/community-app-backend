package com.zonbeozon.channel.service;

import com.zonbeozon.channel.service.dto.InviteCodeResponse;
import com.zonbeozon.member.domain.Member;

public interface ChannelInvitationService {
    InviteCodeResponse publishInvite(Member member, Long channelId, Long inviteeId);
    void inviteAcceptJoinAsMember(Member invitee, String code);
}
