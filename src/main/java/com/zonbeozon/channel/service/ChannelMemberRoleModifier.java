package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberRoleModifier {
    private final ChannelFinder channelFinder;
    private final MemberFinder memberFinder;
    private final ChannelMemberFinder channelMemberFinder;
    private final AuthenticationService authenticationService;
    private final List<ModifyChannelRoleHandler> modifyChannelRoleHandlers;

    public void modifyChannelMemberRole(Long channelId, Long targetMemberId, ChannelRole newRole) {
        Channel channel = channelFinder.findById(channelId);
        Member requestMember = authenticationService.getCurrentMember();
        ChannelMember requestChannelMember = channelMemberFinder.findByMemberAndChannel(requestMember, channel);
        Member targetMember = memberFinder.findById(targetMemberId);
        ChannelMember targetChannelMember = channelMemberFinder.findByMemberAndChannel(targetMember, channel);
        modifyChannelRoleHandlers.stream()
                .filter(handler -> handler.isSupport(newRole))
                .findAny()
                .orElseThrow(()-> new IllegalStateException("조건에 맞는 헨들러가 등록되지 않았습니다"))
                .handle(requestChannelMember, targetChannelMember, newRole);
    }
}
