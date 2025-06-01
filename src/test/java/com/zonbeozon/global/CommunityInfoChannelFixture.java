package com.zonbeozon.global;

import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.service.ChannelCreateCommand;


public class CommunityInfoChannelFixture {
    /**
     * A채널
     * A채널의 구성원은 아래와 같다.
     * member_user_1(channel_A_member_1): Owner.
     * member_user_2(channel_A_member_2), member_admin_1(channel_A_member_3): Member.
     * member_user_3(channel_A_member_4): admin
     */
    public static final CommunityInfoChannel channelA = CommunityInfoChannel.create(
            new ChannelCreateCommand(
                    "보통 채널",
                    "아무 말이나 합니다.",
                    "https://fakeProfile.com",
                    ChannelContentOpenLevel.PUBLIC,
                    ChannelType.COMMUNITY_INFO,
                    ChannelJoinLevel.OPEN,
                    ChannelSearchLevel.PUBLIC
            ),
            MemberFixture.member_user_1
    );


    /**
     * B채널의 구성원은 아래와 같다.
     * member_user_3(channel_B_member_1): Owner.
     */
    public static final CommunityInfoChannel channelB = CommunityInfoChannel.create(
            new ChannelCreateCommand(
                    "B 채널",
                    "아무 말이나 합니다.",
                    "https://fakeProfile.com",
                    ChannelContentOpenLevel.PUBLIC,
                    ChannelType.COMMUNITY_INFO,
                    ChannelJoinLevel.OPEN,
                    ChannelSearchLevel.PUBLIC
            ),
            MemberFixture.member_user_3
    );
}
