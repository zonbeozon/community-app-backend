package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelMemberBannedEvent;
import com.zonbeozon.channel.dto.ChannelMemberUnbannedEvent;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberBanService {
    private final ChannelMemberFinder channelMemberFinder;
    private final ApplicationEventPublisher eventPublisher;

    public void ban(ChannelMemberId channelMemberId) {
        ChannelMember channelMember = channelMemberFinder.findByIdElseThrow(channelMemberId);
        channelMember.updateStatus(ChannelMemberStatus.BANNED);
        eventPublisher.publishEvent(new ChannelMemberBannedEvent(channelMember.getId()));
    }

    public void unban(ChannelMemberId channelMemberId) {
        ChannelMember channelMember = channelMemberFinder.findByIdElseThrowIgnoringStatus(channelMemberId);
        channelMember.updateStatus(ChannelMemberStatus.ACTIVE);
        eventPublisher.publishEvent(new ChannelMemberUnbannedEvent(channelMember.getId()));
    }
}
