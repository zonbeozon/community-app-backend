package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelCreateCommand;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
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
    private final ChannelProfileService channelProfileService;
    private final MemberFinder memberFinder;

    public Long addChannel(Long ownerId, ChannelCreateCommand command) {
        Member Owner = memberFinder.findByIdElseThrow(ownerId);
        if(isDuplicateTitle(command.title()))
            throw new ConflictException(ErrorCode.DUPLICATE_CHANNEL_TITLE);
        Channel channel = channelFactory.createChannel(command, Owner);
        channelRepository.save(channel);
        channelProfileService.updateImage(channel.getId(), command.imageId());
        channelMemberJoiner.joinAsOwner(channel.getId(), ownerId);
        return channel.getId();
    }

    private boolean isDuplicateTitle(String title) {
        return channelRepository.existsByTitle(title);
    }
}
