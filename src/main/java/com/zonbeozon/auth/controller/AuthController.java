package com.zonbeozon.auth.controller;

import com.zonbeozon.auth.AuthenticationTokenUtils;
import com.zonbeozon.auth.dto.AccessTokenIncludedResponse;
import com.zonbeozon.auth.exception.InvalidTokenException;
import com.zonbeozon.auth.jwt.TokenProvider;
import com.zonbeozon.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        tokenService.deleteTokenByMemberId(Long.parseLong(authentication.getName()));
        return ResponseEntity.noContent().build();
    }
 }
