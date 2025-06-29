package com.zonbeozon.post.service;

import com.zonbeozon.channel.service.BaseChannelTest;
import com.zonbeozon.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class PostAddTest extends BaseChannelTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @MockitoBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    @DisplayName("채널에 속해있지 않다면 예외가 발생해야 한다.")
    void sdsd() {

    }

    @Test
    @DisplayName("작성 권한이 없다면 예외가 발생해야 한다.")
    void dsds() {

    }

    @Test
    @DisplayName("이벤트 발생시 이벤트 리스너에서 캐치해 stomp 브로트케스트 한다.")
    void dsd() {

    }


}
