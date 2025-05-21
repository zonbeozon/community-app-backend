package com.zonbeozon.auth.service;

import com.zonbeozon.auth.entity.Token;
import com.zonbeozon.auth.repository.TokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
//
//    @InjectMocks
//    private TokenService tokenService;
//    @Mock
//    private TokenRepository tokenRepository;
//    @Mock
//    private Token token;
//    @Mock
//    private Date date;
//
//    @Test
//    @DisplayName("saveOrUpdates 호출시 기존에 token이 존재하면 기존 토큰의 리프레시 토큰을 업데이트한다.")
//    void updateRefreshToken_WhenTokenExists_ShouldUpdateRefreshToken() {
//        when(tokenRepository.findByAccessToken(eq("accessToken123"))).thenReturn(Optional.of(token));
//        tokenService.saveOrUpdate("123", "accessToken123", "refreshToken123",date);
//        verify(token, times(1)).updateRefreshToken(eq("refreshToken123"), eq(date));
//    }
//
//    @Test
//    @DisplayName("saveOrUpdates 호출시 기존에 token이 없다면 새로운 토큰을 만들어 저장한다.")
//    void saveNewToken_WhenTokenDoesNotExist_ShouldSaveNewToken() {
//        ArgumentCaptor<Token> captor = ArgumentCaptor.forClass(Token.class);
//        when(tokenRepository.findByAccessToken(eq("accessToken123"))).thenReturn(Optional.empty());
//        tokenService.saveOrUpdate("123", "accessToken123", "refreshToken123", date);
//        verify(tokenRepository, times(1)).save(captor.capture());
//        Token capturedToken = captor.getValue();
//
//        Assertions.assertEquals("accessToken123", capturedToken.getAccessToken());
//        Assertions.assertEquals("refreshToken123", capturedToken.getRefreshToken());
//        Assertions.assertEquals(date, capturedToken.getExpiresAt());
//        Assertions.assertEquals("123", capturedToken.getMemberKey());
//    }
}
