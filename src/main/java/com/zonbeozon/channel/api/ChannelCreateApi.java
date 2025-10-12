package com.zonbeozon.channel.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.ChannelCreateCommand;
import com.zonbeozon.channel.dto.ChannelCreateRequest;
import com.zonbeozon.channel.dto.ChannelInfoWithMembershipDto;
import com.zonbeozon.channel.service.ChannelCreator;
import com.zonbeozon.channel.service.assembler.JoinedChannelAssembler;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@ApiComponent
public class ChannelCreateApi {
    private final ChannelCreator channelCreator;
    private final AuthenticationService authenticationService;
    private final ImageOwnershipVerifier imageOwnershipVerifier;
    private final JoinedChannelAssembler joinedChannelAssembler;

    public ChannelInfoWithMembershipDto addCommunityChannel(ChannelCreateRequest request) {
        Member requester = authenticationService.getCurrentMember();
        imageOwnershipVerifier.verify(requester.getId(), request.imageId());
        ChannelCreateCommand command = request.toCommand();
        Long channelId = channelCreator.addChannel(requester.getId(), command);
        return joinedChannelAssembler.getJoinedChannel(channelId, requester.getId());
    }
}
