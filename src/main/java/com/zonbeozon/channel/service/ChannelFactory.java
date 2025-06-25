package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.channel.exception.ChannelAddException;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ChannelFactory {
    private final Map<ChannelType, ChannelCreateStrategy> strategies;

    public Channel createChannel(ChannelCreateCommand channelCreateCommand, Member creator) {
        Channel channel = Optional.ofNullable(strategies.get(channelCreateCommand.type()))
                .orElseThrow(() -> new IllegalStateException(channelCreateCommand.type() + " 을 지원하는 createStrategy가 등록되지 않았습니다"))
                .createChannel(channelCreateCommand, creator);

        if(!channel.isValidSettingCombination()) {
            throw new ChannelAddException(ChannelAddException.ErrorCode.INVALID_CHANNEL_SETTING_COMBINATION);
        }
        return channel;
    }
}


