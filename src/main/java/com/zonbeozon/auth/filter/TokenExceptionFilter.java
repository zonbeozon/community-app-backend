package com.zonbeozon.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.global.exception.dto.ErrorResponse;
import com.zonbeozon.global.exception.UnauthenticatedException;
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
        } catch (UnauthenticatedException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode(), e.getMessage()));

            response.getWriter().write(json);
        }
    }
}
