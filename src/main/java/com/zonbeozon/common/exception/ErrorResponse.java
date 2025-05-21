package com.zonbeozon.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorResponse {
    HttpStatus getStatus();
    String getMessage();
}
