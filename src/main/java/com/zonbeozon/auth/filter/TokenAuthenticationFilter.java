package com.zonbeozon.auth.filter;

import com.zonbeozon.auth.AuthenticationTokenUtils;
import com.zonbeozon.auth.exception.InvalidTokenException;
import com.zonbeozon.auth.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = AuthenticationTokenUtils.resolveAuthHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        //accessToken이 없기 때문에 다음 필터 호출
        if(accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        //accessToken이 유효하다면
        if (tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
            filterChain.doFilter(request, response);
            return;
        }
        //토큰이 만료되었을때
        throw new InvalidTokenException(InvalidTokenException.EXPIRED_MESSAGE);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
