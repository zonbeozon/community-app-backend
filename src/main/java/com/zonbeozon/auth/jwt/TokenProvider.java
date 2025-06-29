package com.zonbeozon.auth.jwt;

import com.zonbeozon.auth.dto.SimpleAuthenticatedPrincipal;
import com.zonbeozon.auth.entity.Token;
import com.zonbeozon.auth.exception.AuthException;
import com.zonbeozon.auth.service.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {
    private final TokenService tokenService;
    private final String key;

    private SecretKey secretKey;

    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    private static final String KEY_ROLE = "role";

    @Autowired
    public TokenProvider(
            TokenService tokenService,
            @Value("${jwt.key}") String key,
            @Value("${jwt.access-token-expire-ms}") long accessTokenExpireTime,
            @Value("${jwt.refresh-token-expire-ms}") long refreshTokenExpireTime
    ) {
        this.tokenService = tokenService;
        this.key = key;
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;

    }

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
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
        tokenService.save(authentication.getName(), accessToken, refreshToken);
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
        Token token = tokenService.getByAccessTokenOrThrow(accessToken);
        String refreshToken = token.getRefreshToken();

        if(validateToken(refreshToken)) {
            String reissueAccessToken = generateAccessToken(getAuthentication(refreshToken));
            tokenService.updatedAccessToken(reissueAccessToken, token);
            return reissueAccessToken;
        }
        //refresh token이 만료된 경우
        tokenService.deleteToken(token);
        throw new AuthException(AuthException.ErrorCode.INVALID_TOKEN);

    }

    /**
     * @throws AuthException 잘못된 토큰일때 하지만 토큰만료는 예외 대신 false를 리턴한다.
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
            throw new AuthException(AuthException.ErrorCode.INVALID_TOKEN);
        }
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(claims.get(KEY_ROLE).toString()));
    }

}
