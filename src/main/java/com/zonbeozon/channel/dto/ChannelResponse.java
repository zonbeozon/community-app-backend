package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;

public record ChannelResponse(
        Long channelId,
        ChannelType channelType,
        String title,
        String profile,
        String description,
        ChannelJoinPolicy channelJoinPolicy,
        ChannelVisibility channelVisibility,
        int memberCount
) {

    public static ChannelResponse from(ChannelWithMemberCount channelWithMemberCount) {
        Channel channel = channelWithMemberCount.getChannel();
        return new ChannelResponse(
                channel.getId(),
                channel.getChannelType(),
                channel.getTitle(),
                channel.getProfile(),
                channel.getDescription(),
                channel.getSetting().getJoinPolicy(),
                channel.getSetting().getVisibility(),
                channelWithMemberCount.getMemberCount()
        );
    }
}
