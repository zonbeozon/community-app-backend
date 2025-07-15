package com.zonbeozon.channel.service;


import com.zonbeozon.channel.dto.ChannelCreateCommand;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;

public interface ChannelCreateStrategy {
    Channel createChannel(ChannelCreateCommand command, Member creator);
    boolean isSupport(ChannelCreateCommand command);
}
