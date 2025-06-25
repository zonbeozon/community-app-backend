package com.zonbeozon.channel.service;

import com.zonbeozon.channel.service.dto.JoinedChannelListResponse;
import com.zonbeozon.channel.service.dto.JoinedChannelResponse;
import com.zonbeozon.member.service.MemberService;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.service.PostService;
import com.zonbeozon.post.service.dto.PostAddCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class ChannelResponseTest extends ChannelServiceTest {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    public ChannelResponseTest(MemberService memberService) {
        super(memberService);
    }

    @Test
    @DisplayName("member가 속한 채널만 가져와야 한다.")
    void d() {
        channelService.addChannel(validChannelCreateCommand_1, serverUser_1);
        channelService.addChannel(validChannelCreateCommand_2, serverUser_2);

        JoinedChannelListResponse response = channelService.createMemberJoinedChannelResponse(serverUser_1);
        assertThat(response.channels()).hasSize(1);
        assertChannelMetadataEquals(response.channels().get(0), validChannelCreateCommand_1);
    }


    @Test
    @DisplayName("최근 Post 작성일 기준 Desc Order로 가져와야 한다.")
    void djlj() {
        Long channelId = channelService.addChannel(validChannelCreateCommand_1, serverUser_1);
        PostAddCommand postAddCommand_1 = new PostAddCommand("post_1");
        PostAddCommand postAddCommand_2 = new PostAddCommand("post_2");
        PostAddCommand postAddCommand_3 = new PostAddCommand("post_3");

        Long postId_1 = postService.addPost(serverUser_1, channelId, postAddCommand_1);
        Long postId_2 = postService.addPost(serverUser_1, channelId, postAddCommand_2);
        Long postId_3 = postService.addPost(serverUser_1, channelId, postAddCommand_3);

        Post post_1 = postRepository.findById(postId_1).get();
        Post post_2 = postRepository.findById(postId_2).get();
        Post post_3 = postRepository.findById(postId_3).get();

        LocalDateTime post_1_createdAt = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime post_2_createdAt = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        LocalDateTime post_3_createdAt = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        ReflectionTestUtils.setField(post_1, "createdAt", post_1_createdAt);
        ReflectionTestUtils.setField(post_2, "createdAt", post_2_createdAt);
        ReflectionTestUtils.setField(post_3, "createdAt", post_3_createdAt);

        postRepository.save(post_1);
        postRepository.save(post_2);
        postRepository.save(post_3);

        JoinedChannelListResponse response = channelService.createMemberJoinedChannelResponse(serverUser_1);

        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).latestPostContent()).isEqualTo(postAddCommand_1.content());
        assertThat(response.channels().get(0).latestPostCreatedAt()).isEqualTo(post_1_createdAt);
    }

    public static void assertChannelMetadataEquals(JoinedChannelResponse response, ChannelCreateCommand command) {
        assertThat(response.title()).isEqualTo(command.title());
        assertThat(response.description()).isEqualTo(command.description());
        assertThat(response.profile()).isEqualTo(command.profile());
        assertThat(response.channelType()).isEqualTo(command.type());
        assertThat(response.channelJoinLevel()).isEqualTo(command.joinLevel());
        assertThat(response.contentOpenLevel()).isEqualTo(command.contentOpenLevel());
        // channelId는 비교 안 하거나, 필요 시 별도로 비교
    }
}
