package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.TestMockImageBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostImageCreateTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostImageService postImageService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PostImageRepository postImageRepository;

    private Channel channel;
    private Post post;
    private Member member;

    @BeforeEach
    void setup() {
        channel = testChannelService.createAndSave();
        member = testMemberService.createAndSave();
        testChannelService.joinAsAdmin(channel, member);
        post = testPostService.createAndSave(channel, member);
    }

    @DisplayName("이미지가 5개가 넘으면 예외가 발생한다.")
    @Test
    void exceedingMaxImageLimitShouldThrowException() {
        List<Long> imageIds = List.of(
                new TestMockImageBuilder(member, "1").persist(entityManager).getId(),
                new TestMockImageBuilder(member, "2").persist(entityManager).getId(),
                new TestMockImageBuilder(member, "3").persist(entityManager).getId(),
                new TestMockImageBuilder(member, "4").persist(entityManager).getId(),
                new TestMockImageBuilder(member, "5").persist(entityManager).getId(),
                new TestMockImageBuilder(member, "6").persist(entityManager).getId()
        );

        Assertions.assertThatThrownBy(() -> postImageService.updatePostImages(post.getId(), imageIds))
                .isInstanceOf(BadRequestException.class)
                .satisfies(e -> {
                    BadRequestException badRequestException = (BadRequestException) e;
                    Assertions.assertThat(badRequestException.getErrorCode()).isEqualTo(ErrorCode.MAX_POST_IMAGE_REACHED.name());
                });
    }

    @DisplayName("이미지가 저장되어야 한다.")
    @Test
    void addPostImagesAndSetSequentialDisplayOrder() {
        List<Long> imageIds = List.of(
                new TestMockImageBuilder(member, "1").persist(entityManager).getId(),
                new TestMockImageBuilder(member, "2").persist(entityManager).getId()
        );

        postImageService.updatePostImages(post.getId(), imageIds);

        List<PostImage> postImages = postImageRepository.findAllByPostIdWithImage(post.getId());
        Assertions.assertThat(postImages).hasSize(2);
        postImages.forEach(postImage -> {
            Assertions.assertThat(postImage.getPost()).isEqualTo(post);
        });
        PostImage firstPostImage = postImages.get(0);
        PostImage secondPostImage = postImages.get(1);
        Assertions.assertThat(firstPostImage.getImage().getObjectKey()).isEqualTo("1");
        Assertions.assertThat(secondPostImage.getImage().getObjectKey()).isEqualTo("2");
    }
}
