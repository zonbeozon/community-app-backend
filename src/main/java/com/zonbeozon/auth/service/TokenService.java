package com.zonbeozon.auth.service;

import com.zonbeozon.auth.dto.SimpleAuthenticatedPrincipal;
import com.zonbeozon.auth.entity.Token;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.UnauthenticatedException;
import com.zonbeozon.auth.repository.TokenRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TokenService implements TokenProvider, TokenValidator, TokenParser {
    private static final String KEY_ROLE = "role";

    private final TokenRepository tokenRepository;
    private final MemberFinder memberFinder;
    private final SecretKey secretKey;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    @Autowired
    public TokenService(
            TokenRepository tokenRepository,
            MemberFinder memberFinder,
            SecretKey secretKey,
            @Value("${jwt.access-token-expire-ms}") long accessTokenExpireTime,
            @Value("${jwt.refresh-token-expire-ms}") long refreshTokenExpireTime
    ) {
        this.tokenRepository = tokenRepository;
        this.memberFinder = memberFinder;
        this.secretKey = secretKey;
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public String generateAccessToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpireTime);
        return generateToken(authentication, now, expiryDate);
    }

    public void generateRefreshToken(Authentication authentication, String accessToken) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpireTime);
        String refreshToken = generateToken(authentication, now, expiryDate);
        save(authentication.getName(), accessToken, refreshToken);
    }

    private String generateToken(Authentication authentication, Date now, Date expiryDate) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
        return Jwts.builder()
                .subject(authentication.getName())
                .claim(KEY_ROLE, authorities)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        AuthenticatedPrincipal authenticatedPrincipal = new SimpleAuthenticatedPrincipal(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(authenticatedPrincipal, null, authorities);
    }

    public String reissueAccessToken(String accessToken) {
        validateToken(accessToken);
        Token token = getByAccessTokenOrThrow(accessToken);
        String refreshToken = token.getRefreshToken();

        if(validateToken(refreshToken)) {
            String reissueAccessToken = generateAccessToken(getAuthentication(refreshToken));
            updatedAccessToken(reissueAccessToken, token);
            return reissueAccessToken;
        }
        //refresh token이 만료된 경우
        deleteToken(token);
        throw new UnauthenticatedException(ErrorCode.INVALID_TOKEN);
    }

    /**
     * @throws UnauthenticatedException 잘못된 토큰일때 하지만 토큰만료는 예외 대신 false를 리턴한다.
     */
    public boolean validateToken(String token) {
        return parseClaims(token).getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_TOKEN);
        }
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(claims.get(KEY_ROLE).toString()));
    }

    public void save(String memberId, String accessToken, String refreshToken) {
        Member member = memberFinder.findById(Long.parseLong(memberId));
        Token token = new Token(member, accessToken, refreshToken);
        tokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public Token getByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new UnauthenticatedException(ErrorCode.INVALID_TOKEN));
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
