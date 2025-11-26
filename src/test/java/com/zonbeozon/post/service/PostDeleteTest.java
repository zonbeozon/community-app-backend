package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.image.TestMockImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostDeletedEvent;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import com.zonbeozon.reaction.post.entity.PostReaction;
import com.zonbeozon.reaction.post.entity.ReactionType;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import jakarta.persistence.Cache;
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

    private Channel channel;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        channel = testChannelService.createAndSave();
        member = testMemberService.createAndSave();
        testChannelService.joinAsOwner(channel, member);

        post = testPostService.createAndSave(channel, member);
    }

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
        Image dummy_image_1 = new TestMockImageBuilder(member, "dummy_1").persist(entityManager);
        Image dummy_image_2 = new TestMockImageBuilder(member, "dummy_2").persist(entityManager);
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
        assertThat(events.get(0).channelId()).isEqualTo(channel.getId());
    }
}
