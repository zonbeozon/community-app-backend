package com.zonbeozon.auth.service;

import org.springframework.security.core.Authentication;

public interface TokenProvider {
    String generateAccessToken(Authentication authentication);
    void generateRefreshToken(Authentication authentication, String accessToken);
    String reissueAccessToken(String accessToken);
}
