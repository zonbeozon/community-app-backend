package com.zonbeozon.communityapp.auth.service;

import com.zonbeozon.communityapp.auth.domain.Token;
import com.zonbeozon.communityapp.auth.exception.TokenException;
import com.zonbeozon.communityapp.auth.repository.TokenRepository;
import com.zonbeozon.communityapp.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {
    private final TokenRepository tokenRepository;

    public void saveOrUpdate(String memberKey, String accessToken, String refreshToken, Date expiresAt) {
        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(t-> t.updateRefreshToken(refreshToken, expiresAt))
                .orElseGet(()->new Token(memberKey, accessToken, refreshToken, expiresAt));

        tokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(ErrorCode.TOKEN_EXPIRED));
    }

    public void updatedAccessToken(String accessToken, Token token) {
        token.setAccessToken(accessToken);
        tokenRepository.save(token);
    }

    public void deleteToken(String memberKey) {
        tokenRepository.deleteByMemberKey(memberKey);
    }

    public void deleteExpiredTokens(Date now) {
        tokenRepository.deleteExpiredTokens(now);
    }
}
