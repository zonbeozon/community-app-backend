package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.ChannelCreateCommand;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelCreator {
    private final ChannelRepository channelRepository;
    private final ChannelFactory channelFactory;
    private final ChannelMemberJoiner channelMemberJoiner;
    private final AuthenticationService authenticationService;
    private final ChannelProfileService channelProfileService;

    public Long addChannel(ChannelCreateCommand command) {
        Member requester = authenticationService.getCurrentMember();
        if(isDuplicateTitle(command.title()))
            throw new ConflictException(ErrorCode.DUPLICATE_CHANNEL_TITLE);
        Channel channel = channelFactory.createChannel(command, requester);
        channelRepository.save(channel);
        channelProfileService.updateImage(channel.getId(), command.imageId());
        channelMemberJoiner.joinAsOwner(requester, channel);
        return channel.getId();
    }

    private boolean isDuplicateTitle(String title) {
        return channelRepository.existsByTitle(title);
    }
}
