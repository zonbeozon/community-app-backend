package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;

public record ChannelCreateCommand (
        String title,
        String description,
        Long imageId,
        ChannelContentVisibility visibility,
        ChannelJoinPolicy joinPolicy
) {
}
