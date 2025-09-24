package com.zonbeozon.channel.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.channel.service.ChannelMemberRoleModifier;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class ChannelMemberRoleModifyApi {
    private final ChannelMemberRoleModifier modifier;
    private final AuthenticationService authenticationService;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;

    public void modifyChannelMemberRole(Long channelId, Long targetMemberId, ChannelRole newRole) {
        if(!channelAuthorizationCheckService.isOwner(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member requester = authenticationService.getCurrentMember();
        modifier.modifyChannelMemberRole(channelId, requester.getId(), targetMemberId, newRole);
    }
}
