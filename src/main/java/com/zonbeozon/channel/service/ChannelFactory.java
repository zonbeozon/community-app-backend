package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ChannelFactory {
    private final Map<ChannelType, ChannelCreateStrategy> strategies;

    public Channel createChannel(ChannelAddCommand channelAddCommand, Member creator) {
        Channel channel = Optional.ofNullable(strategies.get(channelAddCommand.type()))
                .orElseThrow(() -> new IllegalStateException(channelAddCommand.type() + " 을 지원하는 createStrategy가 등록되지 않았습니다"))
                .createChannel(channelAddCommand, creator);

        channel.validateSettingCombination();
        return channel;
    }
}


