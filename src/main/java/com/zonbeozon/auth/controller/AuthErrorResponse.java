package com.zonbeozon.auth.controller;

import com.zonbeozon.auth.exception.ErrorCode;

public record AuthErrorResponse(
        String code,
        String message
) {
    public AuthErrorResponse(ErrorCode errorCode) {
        this(errorCode.getErrorCode(), errorCode.getMessage());
    }
}
