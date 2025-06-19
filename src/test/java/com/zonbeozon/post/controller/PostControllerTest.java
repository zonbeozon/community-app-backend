package com.zonbeozon.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PostController.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
}
