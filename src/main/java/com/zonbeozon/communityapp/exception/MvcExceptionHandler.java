package com.zonbeozon.communityapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MvcExceptionHandler {
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        if(e.getErrorCode().getHttpStatus().is5xxServerError())
            return createResponse(e.getErrorCode().getHttpStatus(), INTERNAL_SERVER_ERROR);
        return createResponse(e.getErrorCode().getHttpStatus(), e.getMessage());
    }

    private ResponseEntity<String> createResponse(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(message);
    }
}
