package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelCreateCommand;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class ChannelFactory {
    private final List<ChannelCreateStrategy> strategies;

    public Channel createChannel(ChannelCreateCommand channelCreateCommand, Member creator) {
         return strategies.stream()
                .filter(strategy->strategy.isSupport(channelCreateCommand))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(channelCreateCommand.type() + " 을 지원하는 createStrategy가 등록되지 않았습니다"))
                .createChannel(channelCreateCommand, creator);
    }
}


