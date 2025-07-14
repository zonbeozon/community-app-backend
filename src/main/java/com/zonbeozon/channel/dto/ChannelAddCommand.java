package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelSearchScope;
import com.zonbeozon.channel.enums.ChannelType;

public record ChannelAddCommand(
        String title,
        String description,
        String profile,
        ChannelContentVisibility contentVisibility,
        ChannelType type,
        ChannelJoinPolicy joinPolicy,
        ChannelSearchScope searchScope,
        ChannelCreatorType creatorType
) {
}
