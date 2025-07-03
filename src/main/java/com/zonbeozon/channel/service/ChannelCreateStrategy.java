package com.zonbeozon.channel.service;


import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;

@FunctionalInterface
public interface ChannelCreateStrategy {
    Channel createChannel(ChannelAddCommand channel, Member creator);
}
