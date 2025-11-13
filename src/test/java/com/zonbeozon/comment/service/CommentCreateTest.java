package com.zonbeozon.comment.service;

import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentCreateTest extends AbstractChannelIntegrationTest {
    @Autowired
    private CommentCreator commentCreator;
    @Autowired
    private CommentRepository commentRepository;

    private Member member;
    private BlogChannel blogChannel;
    private Post post;

    @BeforeEach
    void setup() {
        member = testMemberService.createAndSave();
        blogChannel = testBlogChannelService.createAndSave();
        testBlogChannelService.joinAsOwner(blogChannel, member);
        post = testPostService.createAndSave(blogChannel, member);

    }

    @Test
    @DisplayName("comment 정상 생성")
    void CreateCommentSuccessfully() {
        Long commentId = commentCreator.addComment(member.getId(), post.getId(), "DUUMY");
        Comment found = commentRepository.findById(commentId).orElseThrow();

        Assertions.assertThat(found.getId()).isEqualTo(commentId);
        Assertions.assertThat(found.getContent()).isEqualTo("DUUMY");
    }
}
