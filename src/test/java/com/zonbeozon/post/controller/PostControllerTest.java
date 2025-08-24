package com.zonbeozon.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.SimpleSecurityEnabledWebMvcTest;
import com.zonbeozon.base.AuthorizationCheckDisabledTest;
import com.zonbeozon.post.TestPostCreateRequestBuilder;
import com.zonbeozon.post.dto.PostCreateCommand;
import com.zonbeozon.post.dto.PostCreateRequest;
import com.zonbeozon.post.service.SimplePostAssembler;
import com.zonbeozon.post.service.PostCreator;
import com.zonbeozon.post.service.PostRemover;
import com.zonbeozon.post.service.PostUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SimpleSecurityEnabledWebMvcTest(PostController.class)
public class PostControllerTest extends AuthorizationCheckDisabledTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    private PostCreator postCreator;
    @MockitoBean
    private PostUpdater postUpdater;
    @MockitoBean
    private SimplePostAssembler simplePostAssembler;
    @MockitoBean
    private PostRemover postRemover;

    @BeforeEach
    void setup() {
        Mockito.when(postCreator.addPost(Mockito.anyLong(), Mockito.any(PostCreateCommand.class))).thenReturn(1L);
    }

    @DisplayName("content가 정해진 길이를 만족하지 않는다면 400에러 발생")
    @Test
    @WithMockUser
    void returnBadRequestWhenContentLengthIsInvalid() throws Exception {
        PostCreateRequest request = new TestPostCreateRequestBuilder().withContent("").build();
        mockMvc.perform(
                        post("/channel/1/post")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("생성이 되었다면 201 created 리턴")
    @Test
    @WithMockUser
    void CreatePostSuccessfullyAndReturnPostId() throws Exception {
        PostCreateRequest request = new TestPostCreateRequestBuilder().withContent("fdfdf").build();
        mockMvc.perform(
                        post("/channel/1/post")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1L));
    }

    @DisplayName("정상 조회된다면 200 리턴")
    @Test
    @WithMockUser
    void retrievePostsSuccessfully() throws Exception {
        mockMvc.perform(get("/channel/1/post"))
                .andExpect(status().isOk());
    }
}
