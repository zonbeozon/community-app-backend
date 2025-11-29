package com.zonbeozon.comment.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.comment.dto.CommentDeletedEvent;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentDeleteTest extends AbstractChannelIntegrationTest {
    @Autowired
    private CommentDeleteService commentDeleteService;
    @Autowired
    private CommentRepository commentRepository;

    private Member member;
    private Channel channel;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setup() {
        member = testMemberService.createAndSave();
        channel = testChannelService.createAndSave();
        testChannelService.joinAsOwner(channel, member);
        post = testPostService.createAndSave(channel, member);
        comment = testCommentService.createAndSave(member, post);
    }

    @Test
    @DisplayName("comment 삭제 테스트")
    void deleteCommentSuccessfully() {
        commentDeleteService.delete(comment.getId());
        Assertions.assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    @DisplayName("CommentDeletedEvent 이벤트가 발생한다.")
    void publishCommentDeletedEventWithCorrectDetailsOnDeletion() {
        commentDeleteService.delete(comment.getId());
        List<CommentDeletedEvent> events = applicationEvents.stream(CommentDeletedEvent.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).postId()).isEqualTo(post.getId());
        assertThat(events.get(0).commentId()).isEqualTo(comment.getId());
    }
}
