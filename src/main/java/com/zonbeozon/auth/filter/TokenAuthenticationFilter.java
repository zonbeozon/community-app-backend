package com.zonbeozon.auth.filter;

import com.zonbeozon.auth.AuthenticationTokenUtils;
import com.zonbeozon.auth.service.TokenParser;
import com.zonbeozon.auth.service.TokenValidator;
import com.zonbeozon.config.SecurityPathConfig;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.UnauthenticatedException;
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
    private final TokenParser tokenParser;
    private final TokenValidator tokenValidator;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return SecurityPathConfig.PERMITTED_MATCHER.matches(request);
    }

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
        if (tokenValidator.validateToken(accessToken)) {
            setAuthentication(accessToken);
            filterChain.doFilter(request, response);
        } else {
            throw new UnauthenticatedException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenParser.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
