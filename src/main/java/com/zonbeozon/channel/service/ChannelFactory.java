package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelAddCommand;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class ChannelFactory {
    private final List<ChannelCreateStrategy> strategies;

    public Channel createChannel(ChannelAddCommand channelAddCommand, Member creator) {
         return strategies.stream()
                .filter(strategy->strategy.isSupport(channelAddCommand))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(channelAddCommand.type() + " 을 지원하는 createStrategy가 등록되지 않았습니다"))
                .createChannel(channelAddCommand, creator);
    }
}


