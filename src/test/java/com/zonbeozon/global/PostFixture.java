package com.zonbeozon.global;

import com.zonbeozon.post.entity.Post;

public class PostFixture {
    public static class ChannelA {
        public static final Post post_1 = Post.create(
                "아무 말",
                ChannelMemberFixture.ChannelA.OwnerRole.member_1
        );

        public static final Post post_2 = Post.create(
                "아무 말",
                ChannelMemberFixture.ChannelA.MemberRole.member_3
        );
    }

}
