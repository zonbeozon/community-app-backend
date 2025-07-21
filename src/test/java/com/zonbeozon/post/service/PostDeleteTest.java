package com.zonbeozon.post.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostBuilder;
import com.zonbeozon.post.dto.PostDeletedEvent;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@RecordApplicationEvents
public class PostDeleteTest {
    @Autowired
    private PostRemover postRemover;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ApplicationEvents applicationEvents;
    @Autowired
    private PostRepository postRepository;

    private BlogChannel blogChannel;

    @BeforeEach
    void setUp() {
        blogChannel = (BlogChannel) new TestChannelBuilder().withType(ChannelType.BLOG).persist(entityManager);
    }

    @Test
    @DisplayName("글쓴이가 채널을 나간 상태여도 요청자가 채널 어드민 이상이면 포스트를 삭제할 수 있다.")
    void deletePostWhenRequesterIsAdminAndAuthorHasLeftChannel() {
        Member author = new TestMemberBuilder("author", "author@gmail.com").persist(entityManager);
        Post post = new TestPostBuilder(blogChannel, author).persist(entityManager);

        Member requester = new TestMemberBuilder("requester", "requester@gmail.com").persistAndSetSecurityContext(entityManager);
        new TestChannelMemberBuilder(requester, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
        postRemover.deletePost(post.getId());

        Assertions.assertThat(postRepository.findById(post.getId())).isEmpty();
    }

    @Test
    @DisplayName("요청자의 권한이 글쓴이보다 낮으면 포스트 삭제 시 예외가 발생한다.")
    void throwAccessDeniedExceptionWhenRequesterRoleIsLowerThanAuthor() {
        Member author = new TestMemberBuilder("author", "author@gmail.com").persist(entityManager);
        Post post = new TestPostBuilder(blogChannel, author).persist(entityManager);
        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);

        Member requester = new TestMemberBuilder("requester", "requester@gmail.com").persistAndSetSecurityContext(entityManager);
        new TestChannelMemberBuilder(requester, blogChannel).withRole(ChannelRole.CHANNEL_MEMBER).persist(entityManager);

        assertThatThrownBy(()-> postRemover.deletePost(post.getId())).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("글쓴이라면 삭제 가능하다.")
    void allowPostDeletionWhenRequesterIsAuthor() {
        Member author = new TestMemberBuilder("author", "author@gmail.com").persistAndSetSecurityContext(entityManager);
        Post post = new TestPostBuilder(blogChannel, author).persist(entityManager);
        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);

        postRemover.deletePost(post.getId());

        Assertions.assertThat(postRepository.findById(post.getId())).isEmpty();
    }

    @Test
    @DisplayName("채널에 가입되어 있지 않은 상태라면 예외가 발생한다.")
    void throwAccessDeniedExceptionWhenRequesterIsNotChannelMember() {
        Member author = new TestMemberBuilder("author", "author@gmail.com").persist(entityManager);
        Post post = new TestPostBuilder(blogChannel, author).persist(entityManager);
        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);

        Member requester = new TestMemberBuilder("requester", "requester@gmail.com").persistAndSetSecurityContext(entityManager);
        assertThatThrownBy(()-> postRemover.deletePost(post.getId())).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("PostDeletedEvent 이벤트가 올바른 정보로 발생한다.")
    void publishPostDeletedEventWithCorrectDetailsOnDeletion() {
        Member author = new TestMemberBuilder("author", "author@gmail.com").persistAndSetSecurityContext(entityManager);
        Post post = new TestPostBuilder(blogChannel, author).persist(entityManager);
        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);

        postRemover.deletePost(post.getId());

        List<PostDeletedEvent> events = applicationEvents.stream(PostDeletedEvent.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).postId()).isEqualTo(post.getId());
        assertThat(events.get(0).channelId()).isEqualTo(blogChannel.getId());
    }
}
