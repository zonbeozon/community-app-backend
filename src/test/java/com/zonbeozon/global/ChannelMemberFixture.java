package com.zonbeozon.global;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelRole;

public class ChannelMemberFixture {
    public static class ChannelA {
        public static class OwnerRole {
            public static final ChannelMember member_1 = ChannelMember.create(
                    MemberFixture.member_user_1,
                    CommunityInfoChannelFixture.channelA,
                    ChannelRole.CHANNEL_OWNER
            );
        }
        public static class AdminRole {
            public static final ChannelMember member_2 = ChannelMember.create(
                    MemberFixture.member_user_3,
                    CommunityInfoChannelFixture.channelA,
                    ChannelRole.CHANNEL_ADMIN
            );
        }

        public static class MemberRole {
            public static final ChannelMember member_3 = ChannelMember.create(
                    MemberFixture.member_user_2,
                    CommunityInfoChannelFixture.channelA,
                    ChannelRole.CHANNEL_MEMBER
            );

            public static final ChannelMember member_4 = ChannelMember.create(
                    MemberFixture.member_admin_1,
                    CommunityInfoChannelFixture.channelA,
                    ChannelRole.CHANNEL_MEMBER
            );
        }
    }

    public static class ChannelB {
        public static class OwnerRole {
            public static final ChannelMember member_1 = ChannelMember.create(
                    MemberFixture.member_user_3,
                    CommunityInfoChannelFixture.channelA,
                    ChannelRole.CHANNEL_OWNER
            );

        }
    }




}
