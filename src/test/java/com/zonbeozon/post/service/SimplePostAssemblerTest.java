package com.zonbeozon.post.service;

import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.image.TestMockImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.entity.Post;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SimplePostAssemblerTest extends AbstractChannelIntegrationTest {
    @Autowired
    private SimplePostAssembler simplePostAssembler;
    @Autowired
    private EntityManager entityManager;

    private BlogChannel blogChannel;
    private Member member;

    private Post post_1, post_2, post_3, post_4, post_5;

    private Image image_1, image_2;

    @BeforeEach
    void setup() {
        blogChannel = testBlogChannelService.createAndSave();
        member = testMemberService.createAndSave();
        testBlogChannelService.joinAsAdmin(blogChannel, member);

        post_1 = testPostService.createAndSave(blogChannel, member);
        post_2 = testPostService.createAndSave(blogChannel, member);
        post_3 = testPostService.createAndSave(blogChannel, member);
        post_4 = testPostService.createAndSave(blogChannel, member);
        post_5 = testPostService.createAndSave(blogChannel, member);

        image_1 = new TestMockImageBuilder(member, "1").persist(entityManager);
        image_2 = new TestMockImageBuilder(member, "2").persist(entityManager);

        testPostService.setPostImages(post_1, List.of(image_1, image_2));;
    }

    @DisplayName("커서로 (3번째 포스트 생성시각, 3번째 포스트 ID)쌍이 주어지고 inverted가 false라면, 이전 2개 포스트(2번, 1번)가 조회되어야 한다")
    @Test
    void fetchOlderPostsWhenInvertedIsFalse() {
        CursorBasedPostsResponse response = simplePostAssembler.getCursorBasedPostResponse(
                blogChannel.getId(),
                new PostCursor(post_3.getCreatedAt(), post_3.getId()),
                2,
                false);
        Assertions.assertThat(response.authors()).hasSize(1);
        Assertions.assertThat(response.posts()).hasSize(2);
        Assertions.assertThat(response.posts().get(0).postId()).isEqualTo(post_2.getId());
        Assertions.assertThat(response.posts().get(1).postId()).isEqualTo(post_1.getId());
        Assertions.assertThat(response.nextCursor()).isNull(); //마지막 아이템이기 때문에 다음 cursor는 null
    }

    @DisplayName("조회 사이즈가 1, inverted가 false인 경우, 커서 ID 이전의 포스트 1개만 조회되어야 한다.")
    @Test
    void fetchOneOlderPostWhenSizeIsOne() {
        CursorBasedPostsResponse response = simplePostAssembler.getCursorBasedPostResponse(
                blogChannel.getId(),
                new PostCursor(post_3.getCreatedAt(), post_3.getId()),
                1,
                false
        );
        Assertions.assertThat(response.authors()).hasSize(1);
        Assertions.assertThat(response.posts()).hasSize(1);
        Assertions.assertThat(response.posts().get(0).postId()).isEqualTo(post_2.getId());
        Assertions.assertThat(response.nextCursor()).isEqualTo(new PostCursor(post_2.getCreatedAt(), post_2.getId()));
    }

    @DisplayName("inverted가 true일 때, 커서보다 최신 게시물을 조회하며 결과는 시간기준 내림차순으로 정렬된다")
    @Test
    void fetchNewerPostsInDescWhenInvertedIsTrue() {
        CursorBasedPostsResponse response = simplePostAssembler.getCursorBasedPostResponse(
                blogChannel.getId(),
                new PostCursor(post_2.getCreatedAt(), post_2.getId()),
                10,
                true
        );
        Assertions.assertThat(response.authors()).hasSize(1);
        Assertions.assertThat(response.authors().get(0).memberId()).isEqualTo(member.getId());
        Assertions.assertThat(response.posts()).hasSize(3);
        Assertions.assertThat(response.size()).isEqualTo(3);
        Assertions.assertThat(response.posts().get(0).postId()).isEqualTo(post_5.getId());
        Assertions.assertThat(response.posts().get(1).postId()).isEqualTo(post_4.getId());
        Assertions.assertThat(response.posts().get(2).postId()).isEqualTo(post_3.getId());
        Assertions.assertThat(response.nextCursor()).isNull();
        Assertions.assertThat(response.isLast()).isTrue();
    }
}
