package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.entity.Channel;

public record ChannelResponse(
        Long channelId,
        String title,
        int memberCount,
        String description,
        Channel.Type channelType,
        Channel.OpenLevel openLevel
) {
    public static ChannelResponse from(Channel channel, int memberCount) {
        return new ChannelResponse(
                channel.getId(),
                channel.getTitle(),
                memberCount,
                channel.getDescription(),
                channel.getChannelType(),
                channel.getOpenLevel()
        );
    }
}
