package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelAddCommand;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelSearchScope;
import com.zonbeozon.channel.enums.ChannelType;

public class ChannelFixture {
    public static final ChannelAddCommand CHANNEL_ADD_COMMAND_1 = new ChannelAddCommand(
            "title1",
            "description",
            "emtpyProfile",
            ChannelContentVisibility.PRIVATE,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinPolicy.OPEN,
            ChannelSearchScope.PUBLIC
    );
    public static final ChannelAddCommand CHANNEL_ADD_COMMAND_2 = new ChannelAddCommand(
            "title2",
            "description",
            "emtpyProfile",
            ChannelContentVisibility.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinPolicy.OPEN,
            ChannelSearchScope.PUBLIC
    );

    public static final ChannelAddCommand CHANNEL_ADD_COMMAND_3 = new ChannelAddCommand(
            "title3",
            "description",
            "emtpyProfile",
            ChannelContentVisibility.PUBLIC,
            ChannelType.OFFICIAL_INFO,
            ChannelJoinPolicy.OPEN,
            ChannelSearchScope.PUBLIC
    );
}
