package com.zonbeozon.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ZonbeozonException extends RuntimeException {
    private final ErrorResponse errorResponse;
}
