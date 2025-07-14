package com.zonbeozon.global.exception;

public record ErrorResponse (
    String errorCode,
    String message
) {
}