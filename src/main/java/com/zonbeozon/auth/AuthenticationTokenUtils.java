package com.zonbeozon.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AuthenticationTokenUtils {
    private static final String TOKEN_PREFIX = "Bearer ";

    public static String resolveAuthHeader(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public static String createAuthHeader(String token) {
        return TOKEN_PREFIX + token;
    }

}
