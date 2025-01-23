package com.zonbeozon.communityapp.auth.service;

import com.zonbeozon.communityapp.auth.domain.Token;
import com.zonbeozon.communityapp.auth.exception.TokenException;
import com.zonbeozon.communityapp.auth.repository.TokenRepository;
import com.zonbeozon.communityapp.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    @Transactional
    public void saveOrUpdate(String username, String accessToken, String refreshToken) {
        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(t->t.updateRefreshToken(refreshToken))
                .orElseGet(()->new Token(username, accessToken, refreshToken));

        tokenRepository.save(token);
    }

    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(ErrorCode.TOKEN_EXPIRED));
    }

    @Transactional
    public void updatedAccessToken(String accessToken, Token token) {
        token.setAccessToken(accessToken);
        tokenRepository.save(token);
    }

    @Transactional
    public void deleteToken(String memberKey) {
        tokenRepository.deleteByMemberKey(memberKey);
    }


}
