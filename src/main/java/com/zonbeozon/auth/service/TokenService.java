package com.zonbeozon.auth.service;

import com.zonbeozon.auth.entity.Token;
import com.zonbeozon.auth.exception.AuthException;
import com.zonbeozon.auth.repository.TokenRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {
    private final TokenRepository tokenRepository;
    private final MemberService memberService;

    public void save(String memberId, String accessToken, String refreshToken) {
        Member member = memberService.getByIdOrThrow(Long.parseLong(memberId));
        Token token = new Token(member, accessToken, refreshToken);
        tokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public Token getByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new AuthException(AuthException.ErrorCode.INVALID_TOKEN));
    }

    public void deleteTokenByMemberId(Long memberId) {
        tokenRepository.deleteTokenByMemberId(memberId);
    }

    public void updatedAccessToken(String accessToken, Token token) {
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }

    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }
}
