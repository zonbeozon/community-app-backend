package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelType;

public record JoinedChannelResponse(
        Long channelId,
        String title,
        String profile,
        ChannelType channelType
) {
    public static JoinedChannelResponse from(Channel channel) {
        return new JoinedChannelResponse(
                channel.getId(),
                channel.getTitle(),
                channel.getProfile(),
                channel.getType()
        );
    }
}
