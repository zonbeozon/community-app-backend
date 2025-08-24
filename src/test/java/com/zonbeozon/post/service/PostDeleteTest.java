package com.zonbeozon.post.service;

import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.image.TestImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostBuilder;
import com.zonbeozon.post.dto.PostDeletedEvent;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.enums.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostDeleteTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostRemover postRemover;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PostReactionRepository postReactionRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostImageRepository postImageRepository;

    private BlogChannel blogChannel;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        blogChannel = testBlogChannelService.createAndSave();
        member = testMemberService.createAndSave();
        testBlogChannelService.joinAsOwner(blogChannel, member);

        post = testPostService.createAndSave(blogChannel, member);
    }

    /**
     * 권한 검사 로직은 분리됨
     */
//    @Test
//    @DisplayName("요청자의 권한이 글쓴이보다 낮으면 포스트 삭제 시 예외가 발생한다.")
//    void throwAccessDeniedExceptionWhenRequesterRoleIsLowerThanAuthor() {
//        Member author = new TestMemberBuilder("author", "author@gmail.com").persist(entityManager);
//        Post post = new TestPostBuilder(blogChannel, author).persist(entityManager);
//        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
//
//        Member requester = new TestMemberBuilder("requester", "requester@gmail.com").persistAndSetSecurityContext(entityManager);
//        new TestChannelMemberBuilder(requester, blogChannel).withRole(ChannelRole.CHANNEL_MEMBER).persist(entityManager);
//
//        assertThatThrownBy(()-> postRemover.deletePost(post.getId())).isInstanceOf(AccessDeniedException.class);
//    }

    /**
     * 권한 검사 로직은 분리됨
     */
//    @Test
//    @DisplayName("글쓴이라면 삭제 가능하다.")
//    void allowPostDeletionWhenRequesterIsAuthor() {
//        Member author = new TestMemberBuilder("author", "author@gmail.com").persistAndSetSecurityContext(entityManager);
//        Post post = new TestPostBuilder(blogChannel, author).persist(entityManager);
//        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
//
//        postRemover.deletePost(post.getId());
//
//        Assertions.assertThat(postRepository.findByIdElseThrow(post.getId())).isEmpty();
//    }

    /**
     * 권한 검사 로직은 분리됨
     */
//    @Test
//    @DisplayName("채널에 가입되어 있지 않은 상태라면 예외가 발생한다.")
//    void throwAccessDeniedExceptionWhenRequesterIsNotChannelMember() {
//        Member author = new TestMemberBuilder("author", "author@gmail.com").persist(entityManager);
//        Post post = new TestPostBuilder(blogChannel, author).persist(entityManager);
//        new TestChannelMemberBuilder(author, blogChannel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
//
//        Member requester = new TestMemberBuilder("requester", "requester@gmail.com").persistAndSetSecurityContext(entityManager);
//        assertThatThrownBy(()-> postRemover.deletePost(post.getId())).isInstanceOf(AccessDeniedException.class);
//    }

    @Test
    @DisplayName("post 삭제시 연관된 PostReaction가 삭제 된다.")
    void deleteRelatedReactionsWhenPostIsDeleted() {
        PostReaction postReaction = testPostReactionService.createAndSave(post, ReactionType.LIKE, member);
        postRemover.deletePost(post.getId());

        assertThat(postReactionRepository.findById(postReaction.getId())).isEmpty();
    }

    @Test
    @DisplayName("post 삭제시 연관된 comment가 삭제 된다.")
    void deleteRelatedCommentsWhenPostIsDeleted() {
        Comment comment = testCommentService.createAndSave(member, post);
        postRemover.deletePost(post.getId());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    @DisplayName("post 삭제시 연관된 PostImage가 삭제 된다.")
    void deleteRelatedPostImagesWhenPostIsDeleted() {
        Image dummy_image_1 = new TestImageBuilder(member, "dummy_1").persist(entityManager);
        Image dummy_image_2 = new TestImageBuilder(member, "dummy_2").persist(entityManager);
        List<PostImage> postImages = testPostService.setPostImages(post, List.of(dummy_image_1, dummy_image_2));

        postRemover.deletePost(post.getId());

        assertThat(postImages.size()).isEqualTo(2);
        assertThat(postImageRepository.findById(postImages.get(0).getId())).isEmpty();
        assertThat(postImageRepository.findById(postImages.get(1).getId())).isEmpty();
    }

    @Test
    @DisplayName("PostDeletedEvent 이벤트가 올바른 정보로 발생한다.")
    void publishPostDeletedEventWithCorrectDetailsOnDeletion() {
        postRemover.deletePost(post.getId());

        List<PostDeletedEvent> events = applicationEvents.stream(PostDeletedEvent.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).postId()).isEqualTo(post.getId());
        assertThat(events.get(0).channelId()).isEqualTo(blogChannel.getId());
    }
}
