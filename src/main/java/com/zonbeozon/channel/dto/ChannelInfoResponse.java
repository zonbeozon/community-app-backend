package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelProfile;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.image.entity.ImageResponse;

public record ChannelInfoResponse (
        Long channelId,
        ChannelType channelType,
        String title,
        String description,
        ImageResponse profile,
        ChannelSettingResponse settings,
        Long memberCount
) {
    public static ChannelInfoResponse from(ChannelWithMemberCount channelWithMemberCount) {

        Channel channel = channelWithMemberCount.getChannel();
        ChannelProfile profile = channelWithMemberCount.getChannel().getProfile();
        ImageResponse imageResponse = profile == null ? null : ImageResponse.from(profile.getImage());

        return new ChannelInfoResponse(
                channel.getId(),
                channel.getChannelType(),
                channel.getTitle(),
                channel.getDescription(),
                imageResponse,
                ChannelSettingResponse.from(channel.getSetting()),
                channelWithMemberCount.getMemberCount()
        );
    }
}
