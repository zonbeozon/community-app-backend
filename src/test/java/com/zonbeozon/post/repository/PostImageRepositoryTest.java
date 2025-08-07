package com.zonbeozon.post.repository;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostBuilder;
import com.zonbeozon.post.dto.PostImageCount;
import com.zonbeozon.post.entity.Post;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class PostImageRepositoryTest {
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("postImage 개수가 0개면 count가 0이여야 한다.")
    void countShouldBeZeroWhenPostHasNoImages() {
        Member member = new TestMemberBuilder().persist(entityManager);
        BlogChannel channel = (BlogChannel) new TestChannelBuilder().withType(ChannelType.BLOG).persist(entityManager);
        Post post = new TestPostBuilder(channel, member).persist(entityManager);
        List<PostImageCount> postImageCounts = postImageRepository.countImagesByPostIds(List.of(post.getId()));

        Assertions.assertThat(postImageCounts).hasSize(1);
        Assertions.assertThat(postImageCounts.get(0).getCount()).isEqualTo(0);
    }
}
