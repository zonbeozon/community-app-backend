package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.exception.ChannelMemberNotFoundException;
import com.zonbeozon.channel.service.BaseChannelTest;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.exception.PostAccessDeniedException;
import com.zonbeozon.post.exception.PostBadRequestException;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.dto.PostAddCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class PostAddTest extends BaseChannelTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ChannelMemberService channelMemberService;
    @Autowired
    private ChannelMemberEntityQueryService channelMemberEntityQueryService;

    @Test
    @DisplayName("요청이 올바르다면 정상적으로 저장되어야 한다.")
    void savesPostWhenRequestIsValid() {
        PostAddCommand command = new PostAddCommand("post_1");
        Long id = postService.addPost(channel_1_owner, channel_1_id, new PostAddCommand("post_1"));
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("id에 해당하는 Post가 없습니다"));
        ChannelMember author = channelMemberEntityQueryService.getChannelMemberOrThrow(channel_1_owner, post.getChannel());
        assertThat(post.getContent()).isEqualTo(command.content());
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("맴버가 채널에 속해있지 않다면 예외가 발생한다.")
    void throwsExceptionWhenMemberNotInChannel() {
        assertThatThrownBy(
                () -> postService.addPost(channel_2_owner, channel_1_id, new PostAddCommand("post_1"))
        ).isInstanceOf(ChannelMemberNotFoundException.class);
    }

    @Test
    @DisplayName("POST 작성 가능 채널이 아니라면 예외가 발생한다.")
    void throwsExceptionWhenChannelDoesNotSupportPost() {
        assertThatThrownBy(
                () -> postService.addPost(channel_3_owner, channel_3_id, new PostAddCommand("post_1"))
        ).isInstanceOf(PostBadRequestException.class)
                .satisfies(e -> {
                    PostBadRequestException postBadRequestException = (PostBadRequestException) e;
                    assertThat(postBadRequestException.getErrorCode()).isEqualTo(PostBadRequestException.ErrorCode.POST_NOT_SUPPORTED_CHANNEL);
                });
    }

    @Test
    @DisplayName("작성 권한이 없다면 예외가 발생해야 한다.")
    void throwsExceptionWhenMemberHasNoPostPermission() {
        //member role은 해당 채널에서는 Post작성 권한이 없다.
        channelMemberService.joinAsMember(member_2, channel_1_id);
        assertThatThrownBy(
                () -> postService.addPost(member_2, channel_1_id, new PostAddCommand("post_1"))
        ).isInstanceOf(PostAccessDeniedException.class)
                .satisfies(e -> {
                    PostAccessDeniedException postAccessDeniedException = (PostAccessDeniedException) e;
                    assertThat(postAccessDeniedException.getErrorCode()).isEqualTo(PostAccessDeniedException.ErrorCode.POST_CREATION_DENIED);
                });
    }
}
