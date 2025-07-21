package com.zonbeozon.post.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.channel.service.ChannelMemberJoiner;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostCreateRequestBuilder;
import com.zonbeozon.post.dto.PostCreateCommand;
import com.zonbeozon.post.dto.PostCreateRequest;
import com.zonbeozon.post.dto.PostCreatedEvent;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@RecordApplicationEvents
public class PostCreateTest {
    @Autowired
    private EntityManager entityManager;
    @MockitoBean
    private PostImageCreator postImageCreator;
    @Autowired
    private ApplicationEvents applicationEvents;
    @Autowired
    private PostCreator postCreator;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ChannelMemberJoiner channelMemberJoiner;

    @Autowired
    private ApplicationContext applicationContext;

    private Member author;
    private BlogChannel blogChannel;
    private PostCreateRequest request;

    @BeforeEach
    void setup() {
        author = new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
        blogChannel = (BlogChannel) new TestChannelBuilder().withType(ChannelType.BLOG).persist(entityManager);
        request = new TestPostCreateRequestBuilder().build();
    }

    @Test
    @DisplayName("요청이 올바르다면 정상적으로 저장되어야 한다.")
    void savesPostWhenRequestIsValid() {
        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
        Long id = postCreator.addPost(blogChannel.getId(), new PostCreateCommand(request.content(), request.imageIds()));
        Post post = postRepository.findById(id).get();

        assertThat(post.getContent()).isEqualTo(request.content());
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("맴버가 채널에 속해있지 않다면 예외가 발생한다.")
    void throwsExceptionWhenMemberNotInChannel() {
        assertThatThrownBy(() -> postCreator.addPost(blogChannel.getId(), new PostCreateCommand(request.content(), request.imageIds())))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("POST 작성 가능 채널이 아니라면 예외가 발생한다.")
    void throwsExceptionWhenChannelDoesNotSupportPost() {
        Channel chatChannel = new TestChannelBuilder().withType(ChannelType.CHAT).persist(entityManager);
        new TestChannelMemberBuilder(author, chatChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
        assertThatThrownBy(
                () -> postCreator.addPost(chatChannel.getId(), new PostCreateCommand(request.content(), request.imageIds()))
        ).isInstanceOf(BadRequestException.class)
                .satisfies(e -> {
                    BadRequestException badRequestException = (BadRequestException) e;
                    assertThat(badRequestException.getErrorCode()).isEqualTo(ErrorCode.OPERATION_FOR_BLOG_CHANNEL_ONLY.name());
                });
    }

    @Test
    @DisplayName("이벤트를 발생시킨다.")
    void publishesEventWithCorrectValues() {
        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
        Long id = postCreator.addPost(blogChannel.getId(), new PostCreateCommand(request.content(), request.imageIds()));

        List<PostCreatedEvent> events = applicationEvents.stream(PostCreatedEvent.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).postId()).isEqualTo(id);
        assertThat(events.get(0).channelId()).isEqualTo(blogChannel.getId());
    }
}
