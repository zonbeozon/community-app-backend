package com.zonbeozon.post.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.image.TestImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostBuilder;
import com.zonbeozon.post.TestPostImageBuilder;
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

@SpringBootTest
@Transactional
public class PostImageDeleteTest {
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private PostImageRemover postImageRemover;
    @Autowired
    private EntityManager entityManager;

    private BlogChannel blogChannel;
    private Member member;

    @BeforeEach
    void setup() {
        blogChannel = (BlogChannel) new TestChannelBuilder().withType(ChannelType.BLOG).persist(entityManager);
        member = new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
    }

    @DisplayName("post와 연관된 모든 postImage가 삭제된다.")
    @Test
    void deleteAllPostImagesAssociatedWithGivenPostId() {
        Post post = new TestPostBuilder(blogChannel, member).persist(entityManager);
        Image image = new TestImageBuilder(member, "123").persist(entityManager);
        PostImage postImage_1 = new TestPostImageBuilder(post, image, 0).persist(entityManager);
        PostImage postImage_2 = new TestPostImageBuilder(post, image, 1).persist(entityManager);

        postImageRemover.deletePostImages(post.getId());

        Assertions.assertThat(postImageRepository.findById(postImage_1.getId())).isEmpty();
        Assertions.assertThat(postImageRepository.findById(postImage_2.getId())).isEmpty();
    }
}
