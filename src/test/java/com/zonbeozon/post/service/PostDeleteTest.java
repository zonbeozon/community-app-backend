package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.post.dto.PostEvent;
import com.zonbeozon.post.dto.PostEventMessage;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.image.TestMockImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import com.zonbeozon.reaction.post.entity.PostReaction;
import com.zonbeozon.reaction.post.entity.ReactionType;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostDeleteTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostDeleteService postDeleteService;
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
        postDeleteService.delete(post.getId());

        assertThat(postReactionRepository.findById(postReaction.getId())).isEmpty();
    }

    @Test
    @DisplayName("post 삭제시 연관된 comment가 삭제 된다.")
    void deleteRelatedCommentsWhenPostIsDeleted() {
        Comment comment = testCommentService.createAndSave(member, post);
        postDeleteService.delete(post.getId());
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    @DisplayName("post 삭제시 연관된 PostImage가 삭제 된다.")
    void deleteRelatedPostImagesWhenPostIsDeleted() {
        Image dummy_image_1 = new TestMockImageBuilder(member, "dummy_1").persist(entityManager);
        Image dummy_image_2 = new TestMockImageBuilder(member, "dummy_2").persist(entityManager);
        List<PostImage> postImages = testPostService.setPostImages(post, List.of(dummy_image_1, dummy_image_2));

        postDeleteService.delete(post.getId());

        assertThat(postImages.size()).isEqualTo(2);
        assertThat(postImageRepository.findById(postImages.get(0).getId())).isEmpty();
        assertThat(postImageRepository.findById(postImages.get(1).getId())).isEmpty();
    }
}
