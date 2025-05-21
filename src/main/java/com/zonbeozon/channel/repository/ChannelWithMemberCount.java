package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChannelWithMemberCount {
    private Channel channel;
    private int memberCount;
}
