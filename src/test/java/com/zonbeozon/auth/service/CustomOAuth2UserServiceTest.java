package com.zonbeozon.auth.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;
import java.util.HashMap;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
class CustomOAuth2UserServiceTest {
//    @Autowired
//    private CustomOAuth2UserService customOAuth2UserService;
//
//    @Test
//    void 이미_존재하는_유저는_멤버_리포지토리에서_불러온다() throws OAuth2AuthenticationException {
//        // Given
//        String email = "test@example.com";
//        String memberKey = "12345";
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("email", email);
//
//        Member existingMember = Member.builder()
//                .email(email)
//                .memberKey(memberKey)
//                .role(ServerRole.USER)
//                .build();
//    }
//
//    @Test
//    void 맴버가_리포지토리에_존재하지_않는다면_새롭게_만든다() throws OAuth2AuthenticationException {
//        // Given
//        String email = "test@example.com";
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("email", email);
//        attributes.put("name", "exNickname");
//        attributes.put("role", ServerRole.USER);
//    }
}
