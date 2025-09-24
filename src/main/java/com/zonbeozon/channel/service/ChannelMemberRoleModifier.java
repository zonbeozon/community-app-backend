package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.channel.service.finder.ChannelMemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberRoleModifier {
    private final ChannelMemberFinder channelMemberFinder;
    private final List<ModifyChannelRoleHandler> modifyChannelRoleHandlers;
    private final ChannelFinder channelFinder;

    public void modifyChannelMemberRole(Long channelId, Long requesterId, Long targetMemberId, ChannelRole newRole) {
        channelFinder.findByIdElseThrow(channelId);
        ChannelMember requestChannelMember = channelMemberFinder.findByChannelIdAndMemberIdElseThrow(channelId, requesterId);
        ChannelMember targetChannelMember = channelMemberFinder.findByChannelIdAndMemberIdElseThrow(channelId, targetMemberId);
        modifyChannelRoleHandlers.stream()
                .filter(handler -> handler.isSupport(newRole))
                .findAny()
                .orElseThrow(()-> new IllegalStateException("조건에 맞는 헨들러가 등록되지 않았습니다"))
                .handle(requestChannelMember, targetChannelMember, newRole);
    }
}
