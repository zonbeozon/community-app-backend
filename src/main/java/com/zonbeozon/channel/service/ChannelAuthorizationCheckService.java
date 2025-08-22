package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.CheckReturnValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChannelAuthorizationCheckService {
    private final AuthenticationService authenticationService;
    private final ChannelMemberFinder channelMemberFinder;
    private final ChannelFinder channelFinder;
    private final MemberFinder memberFinder;

    @CheckReturnValue
    public boolean isAtLeastMember(Long channelId) {
        Member member = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        return channelMemberFinder.existsByMemberAndChannel(member, channel);
    }

    @CheckReturnValue
    public boolean isAtLeastAdmin(Long channelId) {
        Member member = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        try {
            return channelMemberFinder.findByMemberAndChannelElseThrow(member, channel).getRole().isAtLeastAdmin();
        } catch (NotFoundException e) {
            return false;
        }
    }

    @CheckReturnValue
    public boolean isOwner(Long channelId) {
        Member member = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        try {
            return channelMemberFinder.findByMemberAndChannelElseThrow(member, channel).getRole().isOwner();
        } catch (NotFoundException e) {
            return false;
        }
    }

    @CheckReturnValue
    public boolean hasHigherRoleThanTargetMember(Long channelId, Long targetMemberId) {
        Member actor = authenticationService.getCurrentMember();
        Member targetMember = memberFinder.findByIdElseThrow(targetMemberId);
        Channel channel = channelFinder.findByIdElseThrow(channelId);

        ChannelRole actorRole;
        try {
            actorRole = channelMemberFinder.findByMemberAndChannelElseThrow(actor, channel).getRole();
        } catch (NotFoundException e) {
            return false;
        }
        ChannelRole targetMemberRole = channelMemberFinder.findByMemberAndChannelElseThrow(targetMember, channel).getRole();

        return actorRole.isHigherThan(targetMemberRole);
    }


    @CheckReturnValue
    public boolean canAccessChannelContent(Long channelId) {
        Member member = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        if(channel.getSetting().getContentVisibility() == ChannelContentVisibility.PUBLIC) return true;
        return channelMemberFinder.existsByMemberAndChannel(member, channel);
    }

}
