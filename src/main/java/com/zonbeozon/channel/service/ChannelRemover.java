package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelDeletedEvent;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelRemover {
    private final ChannelFinder channelFinder;
    private final ApplicationEventPublisher eventPublisher;

    @CheckChannelAccess(ChannelAction.CHANNEL_DELETE)
    public void removeChannel(Long channelId) {
        Channel channel = channelFinder.findById(channelId);
        channel.deleteChannel();
        eventPublisher.publishEvent(new ChannelDeletedEvent(channelId));
    }
}
