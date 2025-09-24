package com.zonbeozon.channel.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.ChannelInfoWithRequesterDto;
import com.zonbeozon.channel.dto.ChannelInfosWithRequesterDto;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.channel.service.assembler.JoinedChannelAssembler;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChannelQueryApi {
    private final JoinedChannelAssembler joinedChannelAssembler;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;
    private final AuthenticationService authenticationService;

    public ChannelInfosWithRequesterDto getJoinedChannels() {
        Member member = authenticationService.getCurrentMember();
        return joinedChannelAssembler.getJoinedChannels(member.getId());
    }

    public ChannelInfoWithRequesterDto getJoinedChannel(Long channelId) {
        if(!channelAuthorizationCheckService.isAtLeastMember(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        return joinedChannelAssembler.getJoinedChannel(channelId, member.getId());
    }
}
