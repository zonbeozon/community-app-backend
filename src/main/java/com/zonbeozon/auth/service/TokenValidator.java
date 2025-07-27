package com.zonbeozon.auth.service;

public interface TokenValidator {
    boolean validateToken(String token);
}
