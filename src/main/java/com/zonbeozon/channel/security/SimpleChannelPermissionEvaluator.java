package com.zonbeozon.channel.security;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.service.ChannelFinder;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimpleChannelPermissionEvaluator {
    private final AuthenticationService authenticationService;
    private final ChannelMemberFinder channelMemberFinder;
    private final ChannelFinder channelFinder;

    public boolean isMemberOfChannel(Long channelId) {
        Member member = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findById(channelId);
        return channelMemberFinder.existsByMemberAndChannel(member, channel);
    }

    public boolean hasMinimumRole(Long channelId, ChannelRole requiredRole) {
        Member requester = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findById(channelId);
        ChannelRole requesterRole = channelMemberFinder.findByMemberAndChannel(requester, channel).getRole();
        //권한이 요구 권한보다 높거나 같다면
        return requesterRole.isHigherThan(requiredRole) || requesterRole.isEqual(requiredRole);
    }

    public boolean isSuperiorTo(Long channelId, Member targetMember) {
        Member requester = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findById(channelId);
        ChannelRole requesterRole = channelMemberFinder.findByMemberAndChannel(requester, channel).getRole();
        ChannelRole targetRole = channelMemberFinder.findByMemberAndChannel(targetMember, channel).getRole();
        //권한이 타겟 맴버보다 더 높다면
        return requesterRole.isHigherThan(targetRole);
    }
}
