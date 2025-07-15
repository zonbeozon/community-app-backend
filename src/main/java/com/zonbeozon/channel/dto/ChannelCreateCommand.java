package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelVisibility;
import com.zonbeozon.channel.enums.ChannelType;

public record ChannelCreateCommand(
        ChannelType type,
        String title,
        String description,
        String profile,
        ChannelVisibility visibility,
        ChannelJoinPolicy joinPolicy,
        ChannelCreatorType creatorType
) {
}
