package com.zonbeozon.post.service;

import com.zonbeozon.channel.service.BaseChannelTest;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.service.dto.PostAddCommand;
import com.zonbeozon.post.service.dto.PostEventResponse;
import com.zonbeozon.post.service.dto.PostEventType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.transaction.TestTransaction;

import static org.assertj.core.api.Assertions.assertThat;


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PostEventTest extends BaseChannelTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @MockitoBean
    private SimpMessagingTemplate messagingTemplate;
    @Test
    @DisplayName("생성 이벤트 발생시 이벤트 리스너에서 캐치해 stomp 브로트케스트 한다.")
    void publishCreatedPostEventToStompBrokerOnPostAdd() {
        ArgumentCaptor<PostEventResponse> captor = ArgumentCaptor.forClass(PostEventResponse.class);
        Long id = postService.addPost(channel_1_owner, channel_1_id, new PostAddCommand("post_1"));
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("id를 찾을 수 없습니다."));
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Mockito.verify(messagingTemplate).convertAndSend(Mockito.eq("/topic/channel/" + post.getChannel().getId() + "/post"), captor.capture());
        PostEventResponse postEventResponse = captor.getValue();
        assertThat(postEventResponse.type()).isEqualTo(PostEventType.CREATED);
        assertThat(postEventResponse.postId()).isEqualTo(id);
    }
}
