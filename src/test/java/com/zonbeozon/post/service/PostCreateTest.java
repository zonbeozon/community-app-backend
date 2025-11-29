package com.zonbeozon.post.service;

import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostCreateRequest;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostCreateTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostCreateService postCreateService;
    @Autowired
    private PostRepository postRepository;

    private Member author;
    private Channel channel;
    private PostCreateRequest request;

    @BeforeEach
    void setup() {
        author = testMemberService.createAndSave();
        channel = testChannelService.createAndSave();
        request = new PostCreateRequest("", List.of());
    }

    @Test
    @DisplayName("요청이 올바르다면 정상적으로 저장되어야 한다.")
    void savesPostWhenRequestIsValid() {
        testChannelService.joinAsOwner(channel, author);
        Long id = postCreateService.createPost(
                author.getId(),
                channel.getId(),
                request.content(),
                request.imageIds()
        );
        Post post = postRepository.findById(id).get();

        assertThat(post.getContent()).isEqualTo(request.content());
        assertThat(post.getAuthor()).isEqualTo(author);
    }
}
