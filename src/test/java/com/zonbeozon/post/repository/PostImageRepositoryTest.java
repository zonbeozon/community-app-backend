package com.zonbeozon.post.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostImageCount;
import com.zonbeozon.post.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostImageRepositoryTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostImageRepository postImageRepository;

    @Test
    @DisplayName("postImage 개수가 0개면 count가 0이여야 한다.")
    void countShouldBeZeroWhenPostHasNoImages() {
        Member member = testMemberService.createAndSave();
        Channel channel = testChannelService.createAndSave();
        testChannelService.joinAsAdmin(channel, member);
        Post post = testPostService.createAndSave(channel, member);
        List<PostImageCount> postImageCounts = postImageRepository.countImagesByPostIds(List.of(post.getId()));

        Assertions.assertThat(postImageCounts).hasSize(1);
        Assertions.assertThat(postImageCounts.get(0).count()).isEqualTo(0);
    }
}
