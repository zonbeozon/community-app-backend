package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.service.ChannelCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChannelCreator channelCreator;

    @Test
    @DisplayName("채널 생성시 Validate 조건 맞지 않으면 예외 발생")
    void nd () {
        mockMvc.perform("")

    }

    @Test
    @DisplayName("채널 생성시 셋팅 값 조합 조건 맞지 않으면 예외 발생")
    void () {

    }
}
