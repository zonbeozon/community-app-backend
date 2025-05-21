package com.zonbeozon.config;

import com.zonbeozon.auth.filter.TokenAuthenticationFilter;
import com.zonbeozon.auth.filter.TokenExceptionFilter;
import com.zonbeozon.auth.handler.CustomAccessDeniedHandler;
import com.zonbeozon.auth.handler.CustomAuthenticationEntryPoint;
import com.zonbeozon.auth.handler.OAuth2FailureHandler;
import com.zonbeozon.auth.handler.OAuth2SuccessHandler;
import com.zonbeozon.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> {
                    oauth2.userInfoEndpoint(c->c.userService(oAuth2UserService))
                            .successHandler(oAuth2SuccessHandler)
                            .failureHandler(new OAuth2FailureHandler());
                })
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass())
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .authorizeHttpRequests(this::configureAuthorization)
                .build();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request) {
        // OPEN PROTECTION LEVEL
        request.requestMatchers(
                "/oauth2/authorization/**",
                "/auth/success",
                "/crypto/**",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        ).permitAll();

        request.requestMatchers(HttpMethod.GET,
                //Discussion Related
                "/discussion/crypto/post/{postId}",
                "/discussion/crypto/post"
        ).permitAll();

        // USER PROTECTION LEVEL
        request.requestMatchers(HttpMethod.POST,
                //Discussion Related
                "/discussion/crypto/post",
                "/discussion/crypto/post/{postId}/comment",
                "/discussion/crypto/post/{postId}/comment/{commentId}/reaction/like",
                "/discussion/crypto/post/{postId}/comment/{commentId}/reaction/dislike",
                "/discussion/crypto/post/{postId}/reaction/like",
                "/discussion/crypto/post/{postId}/reaction/dislike"
        ).hasRole("USER");
        request.requestMatchers(HttpMethod.PATCH,
                //Discussion Related
                "/discussion/crypto/post/{postId}",
                "/discussion/crypto/post/{postId}/comment/{commentId}"
        ).hasRole("USER");
        request.requestMatchers(HttpMethod.DELETE,
                //Discussion Related
                "/discussion/crypto/post/{postId}",
                "/discussion/crypto/post/{postId}/comment/{commentId}",
                "discussion/crypto/post/{postId}/comment/{commentId}/reaction",
                "discussion/crypto/post/{postId}/reaction"
        ).hasRole("USER");


        // ADMIN PROTECTION LEVEL
        request.requestMatchers("/admin/**").hasRole("ADMIN");

        // 정의되지 않은 엔드포인트는 전부 거절
        request.anyRequest().denyAll();
    }
}
