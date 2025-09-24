package com.zonbeozon.auth.api.web;

import com.zonbeozon.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
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
