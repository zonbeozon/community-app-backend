package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelSearchLevel;
import com.zonbeozon.channel.entity.ChannelType;

public class ChannelFixture {
    public static final ChannelAddCommand CHANNEL_ADD_COMMAND_1 = new ChannelAddCommand(
            "title1",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PRIVATE,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );
    public static final ChannelAddCommand CHANNEL_ADD_COMMAND_2 = new ChannelAddCommand(
            "title2",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );

    public static final ChannelAddCommand CHANNEL_ADD_COMMAND_3 = new ChannelAddCommand(
            "title3",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.OFFICIAL_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );
}
