package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.image.entity.ImageResponse;

public record ChannelResponse(
        Long channelId,
        ChannelType channelType,
        String title,
        ImageResponse profile,
        String description,
        ChannelJoinPolicy channelJoinPolicy,
        ChannelContentVisibility channelContentVisibility,
        int memberCount
) {

    public static ChannelResponse from(ChannelWithMemberCount channelWithMemberCount) {
        Channel channel = channelWithMemberCount.getChannel();
        ImageResponse imageResponse = channel.getProfile() == null ? null : ImageResponse.from(channel.getProfile().getImage());
        return new ChannelResponse(
                channel.getId(),
                channel.getChannelType(),
                channel.getTitle(),
                imageResponse,
                channel.getDescription(),
                channel.getSetting().getJoinPolicy(),
                channel.getSetting().getContentVisibility(),
                channelWithMemberCount.getMemberCount()
        );
    }
}
