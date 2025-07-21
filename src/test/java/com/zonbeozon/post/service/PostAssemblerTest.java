package com.zonbeozon.post.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.image.TestImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostBuilder;
import com.zonbeozon.post.TestPostImageBuilder;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PostAssemblerTest {

    @Autowired
    private PostAssembler postAssembler;
    @Autowired
    private EntityManager entityManager;

    private BlogChannel blogChannel;
    private Member member;

    Post post_1, post_2, post_3, post_4, post_5;

    Image image_1, image_2;


    @BeforeEach
    void setup() {
        blogChannel = (BlogChannel) new TestChannelBuilder().withType(ChannelType.BLOG).persist(entityManager);
        member = new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
        //channel join
        new TestChannelMemberBuilder(member, blogChannel).persist(entityManager);

        post_1 = new TestPostBuilder(blogChannel, member).persist(entityManager);
        post_2 = new TestPostBuilder(blogChannel, member).persist(entityManager);
        post_3 = new TestPostBuilder(blogChannel, member).persist(entityManager);
        post_4 = new TestPostBuilder(blogChannel, member).persist(entityManager);
        post_5 = new TestPostBuilder(blogChannel, member).persist(entityManager);

        image_1 = new TestImageBuilder(member, "1").persist(entityManager);
        image_2 = new TestImageBuilder(member, "2").persist(entityManager);
        PostImage postImage_1 = new TestPostImageBuilder(post_1, image_1, 0).persist(entityManager);
        post_1.getImages().add(postImage_1);
        PostImage postImage_2 = new TestPostImageBuilder(post_1, image_2, 1).persist(entityManager);
        post_1.getImages().add(postImage_2);
    }

    @DisplayName("커서 ID로 3번째 포스트 ID가 주어졌을 때, 이전 2개 포스트(2번, 1번)가 조회되어야 한다")
    @Test
    void returnTwoPreviousPostsWhenCursorIsThirdPostId() {
        CursorBasedPostsResponse response = postAssembler.createCursorBasedPostResponse(blogChannel.getId(), post_3.getId(), 2);
        Assertions.assertThat(response.authors()).hasSize(1);
        Assertions.assertThat(response.authors().get(0).memberId()).isEqualTo(member.getId());
        Assertions.assertThat(response.posts()).hasSize(2);
        Assertions.assertThat(response.posts().get(0).postId()).isEqualTo(post_2.getId());
        Assertions.assertThat(response.posts().get(1).postId()).isEqualTo(post_1.getId());
    }

    @DisplayName("조회 사이즈가 1인 경우, 커서 ID 이전의 가장 최근 포스트 1개만 조회되어야 한다.")
    @Test
    void returnOnlyOnePostWhenSizeIsOne() {
        CursorBasedPostsResponse response = postAssembler.createCursorBasedPostResponse(blogChannel.getId(), post_3.getId(), 1);
        Assertions.assertThat(response.authors()).hasSize(1);
        Assertions.assertThat(response.authors().get(0).memberId()).isEqualTo(member.getId());
        Assertions.assertThat(response.posts()).hasSize(1);
        Assertions.assertThat(response.posts().get(0).postId()).isEqualTo(post_2.getId());
    }

    @DisplayName("포스트에 이미지가 포함된 경우, 이미지 URL이 displayOrder 순서대로 포함되어 조회되어야 한다.")
    @Test
    void includeImageUrlsInDisplayOrderWhenPostHasImages() {
        CursorBasedPostsResponse response = postAssembler.createCursorBasedPostResponse(blogChannel.getId(), post_2.getId(), 10);
        Assertions.assertThat(response.authors()).hasSize(1);
        Assertions.assertThat(response.authors().get(0).memberId()).isEqualTo(member.getId());
        Assertions.assertThat(response.posts()).hasSize(1);
        Assertions.assertThat(response.posts().get(0).postId()).isEqualTo(post_1.getId());
        Assertions.assertThat(response.posts().get(0).images()).hasSize(2);
        Assertions.assertThat(response.posts().get(0).images().get(0)).isEqualTo(image_1.getUrl());
        Assertions.assertThat(response.posts().get(0).images().get(1)).isEqualTo(image_2.getUrl());
    }
}
