package com.zonbeozon.auth.repository;

import com.zonbeozon.auth.entity.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TokenRepositoryTest {
//    @Autowired
//    private TokenRepository tokenRepository;
//
//    private Token token1;
//    private Token token2;
//    private Token token3;
//
//    @BeforeEach
//    void setUp() {
//        token1 = Token.builder()
//                .accessToken("accessToken123")
//                .refreshToken("refreshToken123")
//                .memberKey("member123")
//                .expiresAt(new Date(System.currentTimeMillis() + 3600000))
//                .build();
//
//        token2 = Token.builder()
//                .accessToken("accessToken456")
//                .refreshToken("refreshToken456")
//                .memberKey("member456")
//                .expiresAt(new Date(System.currentTimeMillis() - 3600000))
//                .build();
//
//        token3 = Token.builder()
//                .accessToken("accessToken456")
//                .refreshToken("refreshToken456")
//                .memberKey("member456")
//                .expiresAt(new Date(System.currentTimeMillis() + 3600))
//                .build();
//
//        tokenRepository.save(token1);
//        tokenRepository.save(token2);
//        tokenRepository.save(token3);
//    }
//
//    @Test
//    @DisplayName("deleteExpiredTokens 호출 시 만료된 토큰만 삭제하고, 유효한 토큰은 유지된다.")
//    void shouldDeleteOnlyExpiredTokens() {
//        tokenRepository.deleteExpiredTokens(new Date());
//
//        List<Token> tokens = tokenRepository.findAll();
//        Assertions.assertEquals(2, tokens.size());
//        assertThat(tokens).containsExactlyInAnyOrderElementsOf(List.of(token1, token3));
//    }
}
