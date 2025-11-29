package com.zonbeozon.post.recommend;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.post.dto.PagedRecommendPostPayload;
import com.zonbeozon.post.dto.RecommendPostDto;
import com.zonbeozon.post.service.recommend.PostRecommendService;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostMetric;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

public class PostRecommendServiceTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostRecommendService postRecommendService;

    @Test
    @DisplayName("스코어가 높은 순으로 제공한다")
    void recommendShouldReturnPostsOrderedByTotalScoreDesc() {
        Member member = testMemberService.createAndSave();
        Channel channel = testChannelService.createAndSave();
        testChannelService.joinAsAdmin(channel, member);
        PostMetric postMetric_1 = testPostMetricService.createAndSave(1.0, 2.0);
        PostMetric postMetric_2 = testPostMetricService.createAndSave(2.0, 3.0);
        PostMetric postMetric_3 = testPostMetricService.createAndSave(3.0, 4.0);

        Post post_1 = testPostService.createAndSave(channel, member, postMetric_1);
        Post post_2 = testPostService.createAndSave(channel, member, postMetric_2);
        Post post_3 = testPostService.createAndSave(channel, member, postMetric_3);

        PagedRecommendPostPayload response = postRecommendService.recommend(PageRequest.of(0, 2));
        Assertions.assertThat(response.content())
                .extracting(RecommendPostDto::postId)
                .containsExactly(post_3.getId(), post_2.getId());
    }
}
