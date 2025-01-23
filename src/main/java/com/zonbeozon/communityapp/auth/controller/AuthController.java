package com.zonbeozon.communityapp.auth.controller;

import com.zonbeozon.communityapp.auth.domain.Token;
import com.zonbeozon.communityapp.auth.domain.dto.LoginResponse;
import com.zonbeozon.communityapp.auth.jwt.TokenProvider;
import com.zonbeozon.communityapp.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @GetMapping("/auth/success")
    public ResponseEntity<LoginResponse> loginSuccess(@Valid LoginResponse loginResponse) {
        return ResponseEntity.ok(loginResponse);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        tokenService.deleteToken(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
 }
