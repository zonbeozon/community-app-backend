package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelSearchLevel;
import com.zonbeozon.channel.entity.ChannelType;

public class ChannelFixture {
    public static final ChannelCreateCommand channelCreateCommand_1 = new ChannelCreateCommand(
            "title1",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );
    public static final ChannelCreateCommand channelCreateCommand_2 = new ChannelCreateCommand(
            "title2",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );

    public static final ChannelCreateCommand channelCreateCommand_3 = new ChannelCreateCommand(
            "title3",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.OFFICIAL_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );
}
