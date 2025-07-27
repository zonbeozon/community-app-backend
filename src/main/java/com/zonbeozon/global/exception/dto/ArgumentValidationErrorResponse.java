package com.zonbeozon.global.exception.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@JsonPropertyOrder({ "code", "errors" })
public class ArgumentValidationErrorResponse {
    private final String code = "INVALID_ARGUMENT";
    private final List<FieldError> errors;

    @AllArgsConstructor
    @Getter
    public static class FieldError {
        private String field;
        private String message;
    }
}
