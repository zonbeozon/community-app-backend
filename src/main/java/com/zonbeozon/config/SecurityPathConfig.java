package com.zonbeozon.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SecurityPathConfig {
    public static final RequestMatcher PERMITTED_MATCHER = new OrRequestMatcher(
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET,"/ws"),
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.OPTIONS,"/**"),
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET,"/oauth2/authorization/**")
    );

    public static void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request) {
        //OPENED
        request.requestMatchers(PERMITTED_MATCHER).permitAll();
        // ADMIN LEVEL
        request.requestMatchers("/admin/**").hasRole("ADMIN");
        request.anyRequest().authenticated();
    }
}
