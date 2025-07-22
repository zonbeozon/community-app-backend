package com.zonbeozon.post.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.TestImageBuilder;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostBuilder;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class PostImageCreateTest {
    @Autowired
    private PostImageAppender postImageAppender;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PostImageRepository postImageRepository;

    private BlogChannel blogChannel;
    private Post post;
    private Member member;

    @BeforeEach
    void setup() {
        blogChannel = (BlogChannel) new TestChannelBuilder().withType(ChannelType.BLOG).persist(entityManager);
        member = new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
        new TestChannelMemberBuilder(member, blogChannel).persist(entityManager);
        post = new TestPostBuilder(blogChannel, member).persist(entityManager);

    }

    @DisplayName("이미지가 5개가 넘으면 예외가 발생한다.")
    @Test
    void exceedingMaxImageLimitShouldThrowException() {
        List<Long> imageIds = List.of(
                new TestImageBuilder(member, "1").persist(entityManager).getId(),
                new TestImageBuilder(member, "2").persist(entityManager).getId(),
                new TestImageBuilder(member, "3").persist(entityManager).getId(),
                new TestImageBuilder(member, "4").persist(entityManager).getId(),
                new TestImageBuilder(member, "5").persist(entityManager).getId(),
                new TestImageBuilder(member, "6").persist(entityManager).getId()
        );

        Assertions.assertThatThrownBy(() -> postImageAppender.addPostImages(post.getId(), imageIds))
                .isInstanceOf(BadRequestException.class)
                .satisfies(e -> {
                    BadRequestException badRequestException = (BadRequestException) e;
                    Assertions.assertThat(badRequestException.getErrorCode()).isEqualTo(ErrorCode.MAX_POST_IMAGE_REACHED.name());
                });
    }


    @DisplayName("자신이 등록한 이미지가 아니라면 예외가 발생한다.")
    @Test
    void throwAccessDeniedExceptionWhenAddingImageNotOwnedByCurrentUser() {
        Member otherMember = new TestMemberBuilder("yunghi", "yunghi@gmail.com").persist(entityManager);
        List<Long> imageIds = List.of(new TestImageBuilder(otherMember, "1").persist(entityManager).getId());

        Assertions.assertThatThrownBy(() -> postImageAppender.addPostImages(post.getId(), imageIds))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("이미지가 저장되어야 한다.")
    @Test
    void addPostImagesAndSetSequentialDisplayOrder() {
        List<Long> imageIds = List.of(
                new TestImageBuilder(member, "1").persist(entityManager).getId(),
                new TestImageBuilder(member, "2").persist(entityManager).getId()
        );

        postImageAppender.addPostImages(post.getId(), imageIds);

        List<PostImage> postImages = postImageRepository.findAllByPostId(post.getId());
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
