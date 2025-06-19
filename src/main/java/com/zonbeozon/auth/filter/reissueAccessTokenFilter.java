package com.zonbeozon.auth.filter;

import com.zonbeozon.auth.AuthenticationTokenUtils;
import com.zonbeozon.auth.exception.AuthException;
import com.zonbeozon.auth.exception.ErrorCode;
import com.zonbeozon.auth.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class reissueAccessTokenFilter extends OncePerRequestFilter {
    private static final String REISSUE_URI = "/auth/reissue";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.equals(REISSUE_URI)) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = AuthenticationTokenUtils.resolveAuthHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        //accessToken이 없기 때문에 다음 필터 호출
        if(accessToken == null) {
            throw new AuthException(ErrorCode.MISSING_AUTH_HEADER);
        }
        String reissueToken = tokenProvider.reissueAccessToken(accessToken);
        response.setHeader(HttpHeaders.AUTHORIZATION, AuthenticationTokenUtils.createAuthHeader(reissueToken));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
