package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.channel.service.finder.ChannelMemberFinder;
import com.zonbeozon.global.exception.ErrorCode;
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
        channelFinder.findByIdElseThrow(channelId);
        Member member = authenticationService.getCurrentMember();
        return channelMemberFinder.findByChannelIdAndMemberId(channelId, member.getId()).isPresent();
    }

    @CheckReturnValue
    public boolean isAtLeastMember(Long channelId, Long memberId) {
        channelFinder.findByIdElseThrow(channelId);
        Member member = memberFinder.findByIdElseThrow(memberId);
        return channelMemberFinder.findByChannelIdAndMemberId(channelId, member.getId()).isPresent();
    }

    @CheckReturnValue
    public boolean isAtLeastAdmin(Long channelId) {
        channelFinder.findByIdElseThrow(channelId);
        Member member = authenticationService.getCurrentMember();
        try {
            return channelMemberFinder.findByChannelIdAndMemberIdElseThrow(channelId, member.getId()).getRole().isAtLeastAdmin();
        } catch (NotFoundException e) {
            return false;
        }
    }

    @CheckReturnValue
    public boolean isOwner(Long channelId) {
        channelFinder.findByIdElseThrow(channelId);
        Member member = authenticationService.getCurrentMember();
        try {
            return channelMemberFinder.findByChannelIdAndMemberIdElseThrow(channelId, member.getId()).getRole().isOwner();
        } catch (NotFoundException e) {
            return false;
        }
    }

    @CheckReturnValue
    public boolean hasHigherRoleThanTargetMember(Long channelId, Long targetMemberId) {
        channelFinder.findByIdElseThrow(channelId);
        Member actor = authenticationService.getCurrentMember();
        if(!memberFinder.existsById(targetMemberId)) throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        ChannelRole actorRole;
        try {
            actorRole = channelMemberFinder.findByChannelIdAndMemberIdElseThrow(channelId, actor.getId()).getRole();
        } catch (NotFoundException e) {
            return false;
        }
        ChannelRole targetMemberRole = channelMemberFinder.findByChannelIdAndMemberIdElseThrow(channelId, targetMemberId).getRole();
        return actorRole.isHigherThan(targetMemberRole);
    }

    @CheckReturnValue
    public boolean canAccessChannelContent(Long channelId) {
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        if(channel.getSetting().getContentVisibility() == ChannelContentVisibility.PUBLIC) return true;
        return isAtLeastMember(channel.getId());
    }

    @CheckReturnValue
    public boolean canAccessChannelContent(Long channelId, Long memberId) {
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        if(channel.getSetting().getContentVisibility() == ChannelContentVisibility.PUBLIC) return true;
        return isAtLeastMember(channel.getId(), memberId);
    }
}
