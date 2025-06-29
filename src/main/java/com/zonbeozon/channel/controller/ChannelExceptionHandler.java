package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.exception.ChannelAccessDeniedException;
import com.zonbeozon.channel.exception.ChannelBadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.zonbeozon.channel")
public class ChannelExceptionHandler {
    @ExceptionHandler(ChannelBadRequestException.class)
    public ResponseEntity<?> handleChannelBadRequestException(ChannelBadRequestException e) {
        return ResponseEntity.status(ChannelBadRequestException.HTTP_STATUS).body(e.toResponse());
    }

    @ExceptionHandler(ChannelAccessDeniedException.class)
    public ResponseEntity<?> handleChannelUpdateException(ChannelAccessDeniedException e) {
        return ResponseEntity.status(ChannelAccessDeniedException.HTTP_STATUS).body(e.toResponse());
    }
}
