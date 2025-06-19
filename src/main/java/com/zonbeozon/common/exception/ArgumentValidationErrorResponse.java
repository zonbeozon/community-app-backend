package com.zonbeozon.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class ArgumentValidationErrorResponse {
    private final String code = "INVALID_ARGUMENT";
    private List<FieldError> errors;

    @AllArgsConstructor
    @Getter
    public static class FieldError {
        private String field;
        private String message;
    }
}
