package com.zonbeozon.channel.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.ChannelInfoDto;
import com.zonbeozon.channel.dto.ChannelUpdateRequest;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.channel.service.ChannelUpdater;
import com.zonbeozon.channel.service.assembler.ChannelInfoAssembler;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@RequiredArgsConstructor
@Transactional
public class ChannelUpdateApi {
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;
    private final ChannelUpdater channelUpdater;
    private final ChannelInfoAssembler channelInfoAssembler;
    private final ImageOwnershipVerifier imageOwnershipVerifier;
    private final AuthenticationService authenticationService;

    public ChannelInfoDto updateChannel(Long channelId, ChannelUpdateRequest updateRequest) {
        if(!channelAuthorizationCheckService.isOwner(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        imageOwnershipVerifier.verify(member.getId(), updateRequest.imageId());
        channelUpdater.updateChannel(channelId, updateRequest);
        return channelInfoAssembler.getChannelInfo(channelId);
    }
}
