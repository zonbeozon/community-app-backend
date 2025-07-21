package com.zonbeozon.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class UriAuthorizationConfig {
    public static void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request) {
        request.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        // OPEN PROTECTION LEVEL
        request.requestMatchers(
                "/oauth2/authorization/**",
                "/auth/success",
                "/crypto/**"
        ).permitAll();

        request.requestMatchers(HttpMethod.GET,
                //channel Related
                "/channel",
                "/channel/joined",
                "/channel/*/post",
                "/channel/community-blog/joined",

                //member Related
                "/member/*"
        ).authenticated();

        // USER PROTECTION LEVEL
        request.requestMatchers(HttpMethod.POST,
                //channel Related
                "/channel",
                "/channel/*/member",
                "channel/*/post",

                "/image"
        ).authenticated();

        request.requestMatchers(HttpMethod.PATCH,
                //Discussion Related
                "/channel/*",
                "/channel/*/member/*/role",
                "/channel/*/post/*"

        ).authenticated();
        request.requestMatchers(HttpMethod.DELETE,
                //Discussion Related
                "/channel/*/member",
                "/channel/*",
                "/channel/*/member/*/kick",
                "/channel/*/post/*"
        ).authenticated();


        // ADMIN PROTECTION LEVEL
        request.requestMatchers("/admin/**").hasRole("ADMIN");

        // 정의되지 않은 엔드포인트는 전부 거절
        request.anyRequest().denyAll();
    }
}
