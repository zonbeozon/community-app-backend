package com.zonbeozon.global;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieService {
    private final Environment environment;

    public Cookie createCookie(String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);

        if(environment.matchesProfiles("prod")) {
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
        }

        return cookie;
    }

    public Cookie createCookie(String name, String value, int maxAge) {
        return createCookie(name, value, maxAge, "/");
    }
}
