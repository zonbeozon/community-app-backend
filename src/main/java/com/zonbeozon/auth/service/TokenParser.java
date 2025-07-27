package com.zonbeozon.auth.service;

import org.springframework.security.core.Authentication;

public interface TokenParser {
    Authentication getAuthentication(String token);
}
