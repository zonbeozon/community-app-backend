package com.zonbeozon.post.service;

import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostCreateCommand;
import com.zonbeozon.post.dto.PostCreateRequest;
import com.zonbeozon.post.dto.PostCreatedEvent;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostCreateTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostCreator postCreator;
    @Autowired
    private PostRepository postRepository;

    private Member author;
    private BlogChannel blogChannel;
    private PostCreateRequest request;

    @BeforeEach
    void setup() {
        author = testMemberService.createAndSave();
        blogChannel = testBlogChannelService.createAndSave();
        request = new PostCreateRequest("", List.of());
    }

    @Test
    @DisplayName("요청이 올바르다면 정상적으로 저장되어야 한다.")
    void savesPostWhenRequestIsValid() {
        testBlogChannelService.joinAsOwner(blogChannel, author);
        Long id = postCreator.createPost(
                author.getId(),
                blogChannel.getId(),
                new PostCreateCommand(request.content(), request.imageIds())
        );
        Post post = postRepository.findById(id).get();

        assertThat(post.getContent()).isEqualTo(request.content());
        assertThat(post.getAuthor()).isEqualTo(author);
    }

    @Test
    @DisplayName("POST 작성 가능 채널이 아니라면 예외가 발생한다.")
    void throwsExceptionWhenChannelDoesNotSupportPost() {
        Channel chatChannel = testChatChannelService.createAndSave("chat-channel-1");
        testChatChannelService.joinAsOwner(chatChannel, author);
        assertThatThrownBy(
                () -> postCreator.createPost(author.getId(), chatChannel.getId(), new PostCreateCommand(request.content(), request.imageIds()))
        ).isInstanceOf(BadRequestException.class)
                .satisfies(e -> {
                    BadRequestException badRequestException = (BadRequestException) e;
                    assertThat(badRequestException.getErrorCode()).isEqualTo(ErrorCode.OPERATION_FOR_BLOG_CHANNEL_ONLY.name());
                });
    }

    @Test
    @DisplayName("이벤트를 발생시킨다.")
    void publishesEventWithCorrectValues() {
        testBlogChannelService.joinAsOwner(blogChannel, author);
        Long id = postCreator.createPost(
                author.getId(),
                blogChannel.getId(),
                new PostCreateCommand(request.content(), request.imageIds())
        );
        List<PostCreatedEvent> events = applicationEvents.stream(PostCreatedEvent.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).postId()).isEqualTo(id);
        assertThat(events.get(0).channelId()).isEqualTo(blogChannel.getId());
    }
}
