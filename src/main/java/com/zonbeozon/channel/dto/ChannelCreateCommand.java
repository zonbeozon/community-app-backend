package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;

public record ChannelCreateCommand(
        ChannelType type,
        String title,
        String description,
        Long imageId,
        ChannelContentVisibility visibility,
        ChannelJoinPolicy joinPolicy,
        ChannelCreatorType creatorType
) {
}
