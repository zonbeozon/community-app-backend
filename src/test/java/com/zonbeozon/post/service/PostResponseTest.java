package com.zonbeozon.post.service;

import com.zonbeozon.post.repository.PostSort;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.SimplifiedPostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.*;

//public class PostResponseTest extends BasePostTest {
//    @Autowired
//    private PostService postService;
//
//    @Test
//    @DisplayName("채널 조회시 해당 채널의 맴버가 아니고 채널 설정이 컨텐츠 비공개 채널이라면 예외가 발생한다.")
//    void throwExceptionWhenNonMemberTriesToAccessPrivateChannelContent(){
//        assertThatThrownBy(() -> postService.createPagedPostResponse(member_3, channel_1_id, "", 0, 20, PostSort.CREATED_AT, Sort.Direction.DESC))
//                .isInstanceOf(ChannelAccessDeniedException.class)
//                .satisfies(e -> {
//                    ChannelAccessDeniedException exception = (ChannelAccessDeniedException) e;
//                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAccessDeniedException.ErrorCode.CONTENT_READ_FORBIDDEN);
//                });
//    }
//
//    @Test
//    @DisplayName("채널 조회시 올바른 응답 객체를 생성한다.")
//    void returnCorrectPagedPostsResponse(){
//        CursorBasedPostsResponse response = postService.createPagedPostResponse(member_1, channel_1_id, "", 0, 20, PostSort.CREATED_AT, Sort.Direction.DESC);
//        assertThat(response.members().size()).isEqualTo(2);
//        assertThat(response.posts().size()).isEqualTo(3);
//        assertThat(response.posts())
//                .extracting(SimplifiedPostResponse::postId)
//                .containsExactly(
//                        post_3_of_channel_1.getId(), post_2_of_channel_1.getId(), post_1_of_channel_1.getId()
//                );
//    }
//}
