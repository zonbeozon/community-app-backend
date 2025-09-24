package com.zonbeozon.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.SimpleSecurityEnabledWebMvcTest;
import com.zonbeozon.base.AuthorizationCheckDisabledTest;
import com.zonbeozon.global.viewcount.CookieViewMarker;
import com.zonbeozon.global.viewcount.ViewCounter;
import com.zonbeozon.post.api.PostCreateApi;
import com.zonbeozon.post.api.PostDeleteApi;
import com.zonbeozon.post.api.PostQueryApi;
import com.zonbeozon.post.api.PostUpdateApi;
import com.zonbeozon.post.api.web.PostController;
import com.zonbeozon.post.dto.PostCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SimpleSecurityEnabledWebMvcTest(PostController.class)
public class PostControllerTest extends AuthorizationCheckDisabledTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    private PostCreateApi postCreateApi;
    @MockitoBean
    private PostDeleteApi postDeleteApi;
    @MockitoBean
    private PostUpdateApi postUpdateApi;
    @MockitoBean
    private PostQueryApi postQueryApi;
    @MockitoBean("postViewMarker")
    private CookieViewMarker postViewMarker;
    @MockitoBean("postViewCounter")
    private ViewCounter postViewCounter;

    @DisplayName("정상 조회된다면 200 리턴")
    @Test
    @WithMockUser
    void retrievePostsSuccessfully() throws Exception {
        mockMvc.perform(get("/channels/1/posts"))
                .andExpect(status().isOk());
    }
}
