package com.zonbeozon.post.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PostDeleteTest {
    @Autowired
    private PostRemover postRemover;

    @Test
    @DisplayName("글쓴이가 채널을 나간 상태라면 어드민부터 삭제가능하다.")
    void throwsExceptionIfMemberHasNoDeletePermission() {
        assertThatThrownBy(() -> postService.deletePost(member_2, channel_1_id, post_1_of_channel_1.getId()))
                .isInstanceOf(PostAccessDeniedException.class)
                .satisfies(e-> {
                    PostAccessDeniedException exception = (PostAccessDeniedException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(PostAccessDeniedException.ErrorCode.POST_DELETION_DENIED);
                });
    }

    @Test
    @DisplayName("글쓴이가 채널을 나간 상태라면 어드민부터 삭제가능하다.")
    void throwsExceptionIfMemberHasNoDeletePermission() {
        assertThatThrownBy(() -> postService.deletePost(member_2, channel_1_id, post_1_of_channel_1.getId()))
                .isInstanceOf(PostAccessDeniedException.class)
                .satisfies(e-> {
                    PostAccessDeniedException exception = (PostAccessDeniedException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(PostAccessDeniedException.ErrorCode.POST_DELETION_DENIED);
                });
    }

    @Test
    @DisplayName("글쓴이라면 삭제 가능하다.")
    void allowsUpdateWhenMemberIsAuthor() {
        postService.deletePost(member_1, channel_1_id, post_1_of_channel_1.getId());
        assertThat(post_1_of_channel_1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("글쓴이보다 권한이 높다면 삭제 가능하다.")
    void allowsDeleteWhenMemberHasHigherPermissionThanAuthor() {
        postService.deletePost(member_1, channel_1_id, post_3_of_channel_1.getId());
        assertThat(post_3_of_channel_1.isDeleted()).isTrue();
    }
}
