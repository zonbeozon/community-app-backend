package com.zonbeozon.common.exception;

import com.zonbeozon.auth.controller.AuthErrorResponse;
import com.zonbeozon.auth.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public ResponseEntity<?> handleArgumentException(MethodArgumentNotValidException e) {
//        List<ArgumentValidationErrorResponse.FieldError> errors = e.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> new ArgumentValidationErrorResponse.FieldError(
//                        error.getField(),
//                        error.getDefaultMessage()
//                ))
//                .toList();
//
//        ArgumentValidationErrorResponse response = new ArgumentValidationErrorResponse(errors);
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthException(AuthException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new AuthErrorResponse(e.getErrorCode().getErrorCode(), e.getMessage())
        );
    }
}
