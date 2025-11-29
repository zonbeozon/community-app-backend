package com.zonbeozon.channel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.SecurityTestUtils;
import com.zonbeozon.SimpleSecurityEnabledWebMvcTest;
import com.zonbeozon.channel.dto.ChannelSettingRequest;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.test.AuthorizationCheckDisabledTest;
import com.zonbeozon.channel.api.ChannelCreateApi;
import com.zonbeozon.channel.api.ChannelQueryApi;
import com.zonbeozon.channel.api.ChannelRemoveApi;
import com.zonbeozon.channel.api.ChannelUpdateApi;
import com.zonbeozon.channel.api.web.ChannelController;
import com.zonbeozon.channel.dto.ChannelCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SimpleSecurityEnabledWebMvcTest(value = ChannelController.class)
public class ChannelControllerTest extends AuthorizationCheckDisabledTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ChannelCreateApi channelCreateApi;
    @MockitoBean
    private ChannelUpdateApi channelUpdateApi;
    @MockitoBean
    private ChannelQueryApi channelQueryApi;
    @MockitoBean
    private ChannelRemoveApi channelRemoveApi;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("채널 생성")
    class ChannelCreation {
        @Test
        @DisplayName("title field Validate 조건 맞지 않으면 예외 발생")
        @WithMockUser
        void withInvalidTitleShouldReturnBadRequest () throws Exception {
            String invalidTitle = "a";
            ChannelCreateRequest request = new ChannelCreateRequest(
                    invalidTitle,
                    "",
                    null,
                    new ChannelSettingRequest(ChannelContentVisibility.PUBLIC, ChannelJoinPolicy.OPEN)
            );
            mockMvc.perform(
                            post("/channels")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_ARGUMENT"));
        }

        @Test
        @DisplayName("로그인된 사용자자 아니라면 접근 불가")
        void notAuthenticatedShouldReturnUnauthorized() throws Exception {
            SecurityTestUtils.assertNotAuthenticated(mockMvc, HttpMethod.POST, "/channels");
        }
    }
}
