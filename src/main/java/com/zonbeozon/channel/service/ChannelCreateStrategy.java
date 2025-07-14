package com.zonbeozon.channel.service;


import com.zonbeozon.channel.dto.ChannelAddCommand;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;

public interface ChannelCreateStrategy {
    Channel createChannel(ChannelAddCommand command, Member creator);
    boolean isSupport(ChannelAddCommand command);
}
