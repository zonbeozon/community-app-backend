package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.service.BaseChannelTest;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.dto.PostAddCommand;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public abstract class BasePostTest extends BaseChannelTest {
    @Autowired
    private ChannelMemberService channelMemberService;
    @Autowired
    private ChannelMemberEntityQueryService channelMemberEntityQueryService;
    @Autowired
    private ChannelEntityQueryService channelEntityQueryService;
    @Autowired
    private PostEntityQueryService postEntityQueryService;
    @Autowired
    private PostService postService;

    public Post post_1_of_channel_1;
    public Post post_2_of_channel_1;
    public Post post_3_of_channel_1;
    public static LocalDateTime post_1_createAt = LocalDateTime.of(2022, 1, 1, 1, 1);
    public static LocalDateTime post_2_createAt = LocalDateTime.of(2023, 1, 1, 1, 1);
    public static LocalDateTime post_3_createAt = LocalDateTime.of(2024, 1, 1, 1, 1);

    @BeforeEach
    void setup() {
        //member_2를 channel_1에 가입시키고 어드민으로 승격
        channelMemberService.joinAsMember(member_2, channel_1_id);
        Channel channel_1 = channelEntityQueryService.getChannelByIdOrThrow(channel_1_id);
        ChannelMember channelMember_2_of_channel_1 = channelMemberEntityQueryService.getChannelMemberOrThrow(member_2, channel_1);
        channelMemberService.modifyChannelMemberRole(channel_1_owner, channel_1_id, channelMember_2_of_channel_1.getId(), ChannelRole.CHANNEL_ADMIN);
        //채널 1의 Owner: member_1, 채널1의 admin: member_2 으로 Post작성
        Long post_1_id = postService.addPost(member_1, channel_1_id, new PostAddCommand("Post-1"));
        Long post_2_id = postService.addPost(member_1, channel_1_id, new PostAddCommand("Post-2"));
        Long post_3_id = postService.addPost(member_2, channel_1_id, new PostAddCommand("Post-3"));
        post_1_of_channel_1 = postEntityQueryService.getPostByIdOrThrow(post_1_id);
        post_2_of_channel_1 = postEntityQueryService.getPostByIdOrThrow(post_2_id);
        post_3_of_channel_1 = postEntityQueryService.getPostByIdOrThrow(post_3_id);

        ReflectionTestUtils.setField(post_1_of_channel_1, "createdAt", post_1_createAt);
        ReflectionTestUtils.setField(post_2_of_channel_1, "createdAt", post_2_createAt);
        ReflectionTestUtils.setField(post_3_of_channel_1, "createdAt", post_3_createAt);
        ReflectionTestUtils.setField(post_1_of_channel_1, "modifiedAt", post_1_createAt);
        ReflectionTestUtils.setField(post_2_of_channel_1, "modifiedAt", post_2_createAt);
        ReflectionTestUtils.setField(post_3_of_channel_1, "modifiedAt", post_3_createAt);
    }
}
