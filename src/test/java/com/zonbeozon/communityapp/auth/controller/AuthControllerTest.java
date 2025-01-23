package com.zonbeozon.communityapp.auth.controller;

import com.zonbeozon.communityapp.auth.jwt.TokenProvider;
import com.zonbeozon.communityapp.auth.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private TokenProvider tokenProvider;

    @Test
    void 구글_로그인으로_접근하면_구글_로그인_페이지로_리다이렉트_시킨다() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("https://accounts.google.com/o/oauth2/v2/auth")));
    }

    @Test
    @WithMockUser(username = "name")
    void 로그아웃() throws Exception {
        // given
        doNothing().when(tokenService).deleteToken(any());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);

        mockMvc.perform(delete("/auth/logout").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
