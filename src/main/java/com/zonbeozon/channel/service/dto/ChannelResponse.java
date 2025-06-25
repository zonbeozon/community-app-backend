package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.channel.repository.ChannelWithMemberCount;

public record ChannelResponse(
        Long channelId,
        String title,
        String profile,
        String description,
        ChannelType channelType,
        ChannelJoinLevel channelJoinLevel,
        ChannelContentOpenLevel contentOpenLevel,
        int memberCount
) {

    public static ChannelResponse from(ChannelWithMemberCount channelWithMemberCount) {
        Channel channel = channelWithMemberCount.getChannel();
        return new ChannelResponse(
                channel.getId(),
                channel.getTitle(),
                channel.getProfile(),
                channel.getDescription(),
                channel.getType(),
                channel.getJoinLevel(),
                channel.getContentOpenLevel(),
                channelWithMemberCount.getMemberCount()
        );
    }
}
