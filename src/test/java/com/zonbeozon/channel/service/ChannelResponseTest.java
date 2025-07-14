package com.zonbeozon.channel.service;

import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.dto.PostAddCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

//public class ChannelResponseTest extends BaseChannelTest {
//
//    @Autowired
//    private ChannelService channelService;
//    @Autowired
//    private PostService postService;
//    @Autowired
//    private PostRepository postRepository;
//
//    @Test
//    @DisplayName("member가 속한 채널만 가져와야 한다.")
//    void returnsOnlyChannelsJoinedByMember() {
//        ListedChannelResponse response = channelService.createMemberJoinedChannelResponse(channel_1_owner);
//        assertThat(response.channels()).hasSize(1);
//        assertChannelMetadataEquals(response.channels().get(0), ChannelFixture.CHANNEL_ADD_COMMAND_1);
//    }
//
//
//    @Test
//    @DisplayName("최근 Post 작성일 기준 Desc Order로 가져와야 한다.")
//    void sortsChannelsByLatestPostCreatedAtInDescOrder() {
//        PostAddCommand postAddCommand_1 = new PostAddCommand("post_1");
//        PostAddCommand postAddCommand_2 = new PostAddCommand("post_2");
//        PostAddCommand postAddCommand_3 = new PostAddCommand("post_3");
//
//        Long postId_1 = postService.addPost(channel_1_owner, channel_1_id, postAddCommand_1);
//        Long postId_2 = postService.addPost(channel_1_owner, channel_1_id, postAddCommand_2);
//        Long postId_3 = postService.addPost(channel_1_owner, channel_1_id, postAddCommand_3);
//
//        Post post_1 = postRepository.findById(postId_1).get();
//        Post post_2 = postRepository.findById(postId_2).get();
//        Post post_3 = postRepository.findById(postId_3).get();
//
//        LocalDateTime post_1_createdAt = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
//        LocalDateTime post_2_createdAt = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
//        LocalDateTime post_3_createdAt = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
//        ReflectionTestUtils.setField(post_1, "createdAt", post_1_createdAt);
//        ReflectionTestUtils.setField(post_2, "createdAt", post_2_createdAt);
//        ReflectionTestUtils.setField(post_3, "createdAt", post_3_createdAt);
//
//        postRepository.save(post_1);
//        postRepository.save(post_2);
//        postRepository.save(post_3);
//
//        ListedChannelResponse response = channelService.createMemberJoinedChannelResponse(channel_1_owner);
//
//        assertThat(response.channels()).hasSize(1);
//        assertThat(response.channels().get(0).latestPostContent()).isEqualTo(postAddCommand_1.content());
//        assertThat(response.channels().get(0).latestPostCreatedAt()).isEqualTo(post_1_createdAt);
//    }
//
//    @Test
//    @DisplayName("삭제된 채널은 가져오면 안된다")
//    void d() {
//
//    }
//
//    @Test
//    @DisplayName("상태가 ACTIVE인 채널 맴버만 memberCount에 포함한다.")
//    void d() {
//
//    }
//
//    @Test
//    @DisplayName("SearchScope가 NONE인 채널이라면 검색되면 안된다.")
//    void d() {
//
//    }
//
//    public static void assertChannelMetadataEquals(JoinedChannelResponse response, ChannelAddCommand command) {
//        assertThat(response.title()).isEqualTo(command.title());
//        assertThat(response.description()).isEqualTo(command.description());
//        assertThat(response.profile()).isEqualTo(command.profile());
//        assertThat(response.channelType()).isEqualTo(command.type());
//        assertThat(response.channelJoinPolicy()).isEqualTo(command.joinLevel());
//        assertThat(response.contentOpenLevel()).isEqualTo(command.contentOpenLevel());
//    }
//}
