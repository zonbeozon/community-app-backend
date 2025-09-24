package com.zonbeozon.post.repository;

import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostImageCount;
import com.zonbeozon.post.entity.Post;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostImageRepositoryTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("postImage 개수가 0개면 count가 0이여야 한다.")
    void countShouldBeZeroWhenPostHasNoImages() {
        Member member = testMemberService.createAndSave();
        BlogChannel channel = testBlogChannelService.createAndSave();
        testBlogChannelService.joinAsAdmin(channel, member);
        Post post = testPostService.createAndSave(channel, member);
        List<PostImageCount> postImageCounts = postImageRepository.countImagesByPostIds(List.of(post.getId()));

        Assertions.assertThat(postImageCounts).hasSize(1);
        Assertions.assertThat(postImageCounts.get(0).getCount()).isEqualTo(0);
    }
}
