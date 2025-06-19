package com.zonbeozon.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.auth.controller.AuthErrorResponse;
import com.zonbeozon.auth.exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * TokenAuthenticationFilter 이전에 실행되어 이후 필터에서 발생한 예외를 처리한다.
 */
public class TokenExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(new AuthErrorResponse(e.getErrorCode()));

            response.getWriter().write(json);
        }
    }
}
