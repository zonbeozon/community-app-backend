package com.zonbeozon.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticatedPrincipal;

@Getter
@RequiredArgsConstructor
public class SimpleAuthenticatedPrincipal implements AuthenticatedPrincipal {
    private final String name;
}
