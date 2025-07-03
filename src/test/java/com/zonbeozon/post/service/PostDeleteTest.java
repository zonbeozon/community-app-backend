package com.zonbeozon.post.service;

import com.zonbeozon.channel.exception.ChannelAccessDeniedException;
import com.zonbeozon.post.exception.PostAccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(NoOpEventPublisherTestConfig.class)
public class PostDeleteTest extends BasePostTest {
    @Autowired
    private PostService postService;

    @Test
    @DisplayName("post가 속한 채널과 받은 채널 id가 다르면 예외가 발생한다.")
    void throwsExceptionIfChannelIdDoesNotMatchPostChannel() {
        assertThatThrownBy(() -> postService.deletePost(member_2, channel_2_id, post_1_of_channel_1.getId()))
                .isInstanceOf(ChannelAccessDeniedException.class)
                .satisfies(e-> {
                    ChannelAccessDeniedException exception = (ChannelAccessDeniedException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAccessDeniedException.ErrorCode.CHANNEL_MEMBER_MISMATCH);
                });
    }

    @Test
    @DisplayName("삭제 권한이 없으면 예외가 발생한다.")
    void throwsExceptionIfMemberHasNoDeletePermission() {
        //Member2는 channel_1의 admin이지만 post_1은 channel1의 owner가 작성한 글이기 떄문에 권한이 없다.
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
