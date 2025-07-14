package com.zonbeozon.post.service;

import com.zonbeozon.post.exception.PostAccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class PostUpdateTest extends BasePostTest {
    @Autowired
    private PostService postService;

    @Test
    @DisplayName("post가 속한 채널과 받은 채널 id가 다르면 예외가 발생한다.")
    void throwsExceptionIfChannelIdDoesNotMatchPostChannel() {
        assertThatThrownBy(() -> postService.updatePostContent(member_2, channel_2_id, post_1_of_channel_1.getId(), "update"))
                .isInstanceOf(ChannelAccessDeniedException.class)
                .satisfies(e-> {
                    ChannelAccessDeniedException exception = (ChannelAccessDeniedException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAccessDeniedException.ErrorCode.CHANNEL_MEMBER_MISMATCH);
                });
    }

    @Test
    @DisplayName("업데이트 권한이 없으면 예외가 발생한다.")
    void throwsExceptionIfMemberHasNoUpdatePermission() {
        //Member2는 channel_1의 admin이지만 post_1은 channel1의 owner가 작성한 글이기 떄문에 권한이 없다.
        assertThatThrownBy(() -> postService.updatePostContent(member_2, channel_1_id, post_1_of_channel_1.getId(), "update"))
                .isInstanceOf(PostAccessDeniedException.class)
                .satisfies(e-> {
                    PostAccessDeniedException exception = (PostAccessDeniedException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(PostAccessDeniedException.ErrorCode.POST_UPDATE_DENIED);
                });
    }

    @Test
    @DisplayName("글쓴이라면 업데이트 가능하다.")
    void allowsUpdateWhenMemberIsAuthor() {
        String updateContent = "update";
        postService.updatePostContent(member_1, channel_1_id, post_1_of_channel_1.getId(), updateContent);
        assertThat(post_1_of_channel_1.getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("글쓴이보다 권한이 높아도 업데이트시 예외가 발생한다..")
    void throwsExceptionWhenUpdatingPostEvenIfHasHigherRoleThanAuthor() {
        String updateContent = "update";
        assertThatThrownBy(() -> postService.updatePostContent(member_1, channel_1_id, post_3_of_channel_1.getId(), updateContent))
                .isInstanceOf(PostAccessDeniedException.class)
                .satisfies(e-> {
                    PostAccessDeniedException exception = (PostAccessDeniedException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(PostAccessDeniedException.ErrorCode.POST_UPDATE_DENIED);
                });
    }
}
