package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.exception.ChannelAddBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.zonbeozon.channel")
public class ChannelExceptionHandler {
    @ExceptionHandler(ChannelAddBadRequestException.class)
    public ResponseEntity<?> handleChannelBadRequestException(ChannelAddBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toResponse());
    }
}
