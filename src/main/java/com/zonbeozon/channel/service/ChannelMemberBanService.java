package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelMemberBannedEvent;
import com.zonbeozon.channel.entity.BannedChannelMember;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.repository.BannedChannelMemberRepository;
import com.zonbeozon.channel.service.finder.ChannelMemberFinder;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberBanService {
    private final ChannelMemberFinder channelMemberFinder;
    private final ApplicationEventPublisher eventPublisher;
    private final BannedChannelMemberRepository bannedChannelMemberRepository;
    private final ChannelMemberRemover channelMemberRemover;

    public void ban(Long channelId, Long memberId, @Nullable String reason) {
        ChannelMember channelMember = channelMemberFinder.findByChannelIdAndMemberIdWithChannelAndMemberElseThrow(channelId, memberId);
        bannedChannelMemberRepository.save(new BannedChannelMember(channelMember.getChannel(), channelMember.getMember(), reason));
        channelMemberRemover.deleteChannelMember(channelId, memberId);
        eventPublisher.publishEvent(new ChannelMemberBannedEvent(channelId, memberId));
    }

    public void unban(Long channelId, Long memberId) {
        BannedChannelMember bannedChannelMember = bannedChannelMemberRepository.findByChannelIdAndMemberId(channelId, memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_IS_NOT_BAN_STATUS));
        bannedChannelMemberRepository.delete(bannedChannelMember);
    }
}
