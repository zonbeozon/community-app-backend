package com.zonbeozon.auth.controller;

import com.zonbeozon.auth.exception.AuthException;

public record AuthErrorResponse(
        String code,
        String message
) {
    public AuthErrorResponse(AuthException.ErrorCode errorCode) {
        this(errorCode.getErrorCode(), errorCode.getMessage());
    }
}
