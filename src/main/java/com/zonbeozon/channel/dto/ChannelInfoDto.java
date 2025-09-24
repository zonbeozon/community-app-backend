package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.image.dto.ImageDto;

public record ChannelInfoDto(
        Long channelId,
        ChannelType channelType,
        ChannelCreatorType creatorType,
        String title,
        String description,
        ImageDto profile,
        ChannelSettingDto settings,
        Long memberCount
) {
    public ChannelInfoDto(
            Long channelId,
            ChannelType channelType,
            ChannelCreatorType creatorType,
            String title,
            String description,
            Long imageId,
            String imageUrl,
            ChannelSettingDto settings,
            Long memberCount
    ) {
        this(
                channelId,
                channelType,
                creatorType,
                title,
                description,
                imageId == null ? null : new ImageDto(imageId, imageUrl),
                settings,
                memberCount
        );
    }
}
