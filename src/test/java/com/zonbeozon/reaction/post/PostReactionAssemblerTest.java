package com.zonbeozon.reaction.post;

import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.post.dto.PostReactionCountDto;
import com.zonbeozon.reaction.post.entity.ReactionType;
import com.zonbeozon.reaction.post.service.PostReactionAssembler;
import com.zonbeozon.reaction.post.service.PostReactionMarker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class PostReactionAssemblerTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostReactionAssembler postReactionAssembler;
    @Autowired
    private PostReactionMarker postReactionMarker;

    private BlogChannel blogChannel;
    private Member requester;
    private Member otherMember;

    @BeforeEach
    void setUp() {
        blogChannel = testBlogChannelService.createAndSave();
        requester = testMemberService.createAndSave();
        otherMember = testMemberService.createAndSave("otherMember");
        testBlogChannelService.joinAsMember(blogChannel, requester);
        testBlogChannelService.joinAsMember(blogChannel, otherMember);
    }

    @Test
    @DisplayName("포스트별 리엑션 개수 집계 확인")
    void aggregateReactionCountsCorrectly() {
        Post post_1 = testPostService.createAndSave(blogChannel, requester);
        Post post_2 = testPostService.createAndSave(blogChannel, otherMember);

        postReactionMarker.mark(requester.getId(), post_1.getId(), ReactionType.LIKE);
        postReactionMarker.mark(requester.getId(), post_2.getId(), ReactionType.LIKE);
        postReactionMarker.mark(otherMember.getId(), post_1.getId(), ReactionType.DISLIKE);

        Map<Long, PostReactionCountDto> counts = postReactionAssembler.getReactionCountByPostIdIn(List.of(post_1.getId(), post_2.getId()));
        Assertions.assertThat(counts).hasSize(2);
        Assertions.assertThat(counts.get(post_1.getId()).likeCount()).isEqualTo(1);
        Assertions.assertThat(counts.get(post_1.getId()).dislikeCount()).isEqualTo(1);
        Assertions.assertThat(counts.get(post_2.getId()).likeCount()).isEqualTo(1);
        Assertions.assertThat(counts.get(post_2.getId()).dislikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("존재하지 않는 postId가 한개라도 존재하면 예외가 발생한다.")
    void throwNotFoundExceptionWhenAnyPostIdDoesNotExist() {
        Post post_1 = testPostService.createAndSave(blogChannel, requester);
        Assertions.assertThatThrownBy(() -> postReactionAssembler.getReactionCountByPostIdIn(List.of(post_1.getId(), -1L)))
                .isInstanceOf(NotFoundException.class);
    }
}
