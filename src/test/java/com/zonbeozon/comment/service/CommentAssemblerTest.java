package com.zonbeozon.comment.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.comment.dto.CommentsWithAuthorResponse;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentAssemblerTest extends AbstractChannelIntegrationTest {
    @Autowired
    private CommentAssembler commentAssembler;

    private Member member;
    private Channel channel;
    private Post post;

    @BeforeEach
    void setup() {
        member = testMemberService.createAndSave();
        channel = testChannelService.createAndSave();
        testChannelService.joinAsOwner(channel, member);
        post = testPostService.createAndSave(channel, member);

    }

    @DisplayName("postId에 연관되어 있는 comment가 CreateAt Desc Order로 호출된다.")
    @Test
    void getCommentsForPostInDescOrder() {
        Comment comment_1 = testCommentService.createAndSave(member, post);
        Comment comment_2 = testCommentService.createAndSave(member, post);
        CommentsWithAuthorResponse response = commentAssembler.getCommentResponseByPostId(post.getId());

        Assertions.assertThat(response.authors().size()).isEqualTo(1);
        Assertions.assertThat(response.comments().size()).isEqualTo(2);
        //createdAt DESC order
        Assertions.assertThat(response.comments().get(0).commentId()).isEqualTo(comment_2.getId());
        Assertions.assertThat(response.comments().get(1).commentId()).isEqualTo(comment_1.getId());
    }
}
