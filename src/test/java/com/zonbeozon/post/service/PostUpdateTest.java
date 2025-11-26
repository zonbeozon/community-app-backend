package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.post.domain.metric.PostMetric;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.image.ImageRepository;
import com.zonbeozon.image.TestMockImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostUpdateRequest;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostUpdateTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostUpdater postUpdater;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private PostImageRepository postImageRepository;

    private Member member;
    private Channel channel;
    private Post post;
    private List<Image> images;

    @BeforeEach
    void setUp() {
        member = testMemberService.createAndSave();
        channel = testChannelService.createAndSave();
        testChannelService.joinAsAdmin(channel, member);
        images = List.of(
            new TestMockImageBuilder(member, "1").build(),
            new TestMockImageBuilder(member, "2").build(),
            new TestMockImageBuilder(member, "3").build()
        );
        imageRepository.saveAll(images);
        post = testPostService.createAndSave("", images, channel, member, new PostMetric());
    }

    @Test
    @DisplayName("이미지 업데이트 테스트")
    void testUpdateImage() {
        Image newImage = new TestMockImageBuilder(member, "4").build();
        imageRepository.save(newImage);
        List<Image> imagesToUpdate = List.of(
                images.get(0),
                images.get(1),
                newImage
        );
        postUpdater.updateContent(post.getId(), new PostUpdateRequest("", imagesToUpdate.stream().map(Image::getId).toList()));

        List<PostImage> postImages = postImageRepository.findAllByPostIdWithImage(post.getId());
        Assertions.assertThat(postImages).hasSize(3);
        List<Image> images = postImages.stream().map(PostImage::getImage).toList();
        Assertions.assertThat(images).hasSize(3)
                .map(Image::getId)
                .containsExactlyInAnyOrderElementsOf(imagesToUpdate.stream().map(Image::getId).toList());
    }
}
