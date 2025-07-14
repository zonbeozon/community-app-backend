package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;

public record ChannelResponse(
        Long channelId,
        String title,
        String profile,
        String description,
        ChannelType channelType,
        ChannelJoinPolicy channelJoinPolicy,
        ChannelContentVisibility contentOpenLevel,
        int memberCount
) {

    public static ChannelResponse from(ChannelWithMemberCount channelWithMemberCount) {
        Channel channel = channelWithMemberCount.getChannel();
        return new ChannelResponse(
                channel.getId(),
                channel.getTitle(),
                channel.getProfile(),
                channel.getDescription(),
                channel.getChannelType(),
                channel.getSetting().getJoinPolicy(),
                channel.getSetting().getContentVisibility(),
                channelWithMemberCount.getMemberCount()
        );
    }
}
