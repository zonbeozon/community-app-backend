package com.zonbeozon.config;

import com.zonbeozon.auth.handler.OAuth2SuccessHandler;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.service.MemberCreator;
import com.zonbeozon.member.service.MemberFinder;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Profile("local")
@Configuration
@RequiredArgsConstructor
public class LocalSecurityConfig {
    private final MemberCreator memberCreator;
    private final OAuth2SuccessHandler successHandler;
    private final MemberFinder memberFinder;

    @Bean
    @Order(0)
    public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/local/**")
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/local/swagger-ui/**", "/local/v3/api-docs/**", "/local/login").permitAll();
                })
                .addFilterBefore(new LocalTestLoginFilter(successHandler, memberFinder), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @PostConstruct
    public void init() {
        memberCreator.createMemberWithRandomUsername("user_1@gmail.com", ServerRole.USER);
        memberCreator.createMemberWithRandomUsername("user_2@gmail.com", ServerRole.USER);
        memberCreator.createMemberWithRandomUsername("user_3@gmail.com", ServerRole.USER);
        memberCreator.createMemberWithRandomUsername("admin@gmail.com", ServerRole.ADMIN);
    }

    @RequiredArgsConstructor
    public static class LocalTestLoginFilter extends OncePerRequestFilter {
        private final OAuth2SuccessHandler successHandler;
        private final MemberFinder memberFinder;
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            if (request.getRequestURI().equals("/local/login")) {
                // testuser 로 자동 인증
                String email = request.getParameter("email");
                Member member = memberFinder.findByEmail(email).orElseThrow();
                Authentication authentication = createAuthentication(member);
                successHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }
            filterChain.doFilter(request, response);
        }

        private Authentication createAuthentication(Member member) {
            return new Authentication() {

                @Override
                public String getName() {
                    return member.getId().toString();
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return List.of(new SimpleGrantedAuthority(member.getRole().getKey()));
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return null;
                }

                @Override
                public boolean isAuthenticated() {
                    return true;
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                }
            };
        }
    }
}
