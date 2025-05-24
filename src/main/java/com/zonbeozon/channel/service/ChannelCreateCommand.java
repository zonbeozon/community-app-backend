package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelSearchLevel;
import com.zonbeozon.channel.entity.ChannelType;

public record ChannelCreateCommand(
        String title,
        String description,
        String profile,
        ChannelContentOpenLevel contentOpenLevel,
        ChannelType type,
        ChannelJoinLevel joinLevel,
        ChannelSearchLevel searchLevel
) {
}
