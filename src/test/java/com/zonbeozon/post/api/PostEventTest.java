package com.zonbeozon.post.api;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostCreateRequest;
import com.zonbeozon.post.dto.PostEvent;
import com.zonbeozon.post.dto.PostUpdateRequest;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostEventTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostCommendApi postCommendApi;

    private Channel channel;
    private Member member;

    @BeforeEach
    void setup() {
        channel = testChannelService.createAndSave();
        member = testMemberService.createAndSave();
        testMemberService.setSecurityContext(member);
        testChannelService.joinAsOwner(channel, member);
    }

    @Test
    @DisplayName("생성 이벤트를 발생시킨다.")
    void publishesCreatedEventWithCorrectValue() {
        Long id = postCommendApi.createPost(
                channel.getId(),
                new PostCreateRequest("content", List.of())
        );
        List<PostEvent.Created> events = applicationEvents.stream(PostEvent.Created.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).postId).isEqualTo(id);
        assertThat(events.get(0).channelId).isEqualTo(channel.getId());
    }

    @Test
    @DisplayName("삭제 이벤트를 발생시킨다.")
    void publishesDeletedEventWithCorrectValue() {
        Long id = postCommendApi.createPost(
                channel.getId(),
                new PostCreateRequest("content", List.of())
        );
        postCommendApi.deletePost(id);
        List<PostEvent.Deleted> events = applicationEvents.stream(PostEvent.Deleted.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).postId).isEqualTo(id);
        assertThat(events.get(0).channelId).isEqualTo(channel.getId());
    }

    @Test
    @DisplayName("업데이트 이벤트를 발생시킨다.")
    void publishesUpdatedEventWithCorrectValue() {
        Long id = postCommendApi.createPost(
                channel.getId(),
                new PostCreateRequest("content", List.of())
        );
        postCommendApi.updatePost(
                id,
                new PostUpdateRequest("update-content", List.of())
        );
        List<PostEvent.Updated> events = applicationEvents.stream(PostEvent.Updated.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).postId).isEqualTo(id);
        assertThat(events.get(0).channelId).isEqualTo(channel.getId());
    }
}
