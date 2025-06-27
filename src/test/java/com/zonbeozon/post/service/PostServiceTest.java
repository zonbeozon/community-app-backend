package com.zonbeozon.post.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberService;
import com.zonbeozon.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostRepository postRepository;
    @MockitoBean
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    void setup() {
        //event publish off
        lenient().doNothing().when(applicationEventPublisher).publishEvent(any());
    }

    @Nested
    @DisplayName("Post 작성 테스트")
    class PostCreateTest {
        private Member member;

        @BeforeEach
        void setup() {

        }
    }
}
