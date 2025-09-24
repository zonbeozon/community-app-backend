package com.zonbeozon.channel.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.enums.JoinResultStatus;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.channel.service.ChannelMemberJoiner;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class ChannelMemberJoinApi {
    private final ChannelMemberJoiner channelMemberJoiner;
    private final AuthenticationService authenticationService;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;

    public JoinResultStatus joinAsMember(Long channelId) {
        Member requester = authenticationService.getCurrentMember();
        return channelMemberJoiner.joinAsMember(channelId, requester.getId());
    }

    public void approveJoinRequest(Long channelId, Long requesterId) {
        if(!channelAuthorizationCheckService.isOwner(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberJoiner.approveJoinRequest(channelId, requesterId);
    }

    public void denyJoinRequest(Long channelId, Long requesterId) {
        if(!channelAuthorizationCheckService.isOwner(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberJoiner.denyJoinRequest(channelId, requesterId);
    }
}
