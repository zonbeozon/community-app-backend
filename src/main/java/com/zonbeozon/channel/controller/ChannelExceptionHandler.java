package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.exception.ChannelAddException;
import com.zonbeozon.channel.exception.ChannelDeleteException;
import com.zonbeozon.channel.exception.ChannelUpdateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.zonbeozon.channel")
public class ChannelExceptionHandler {
    @ExceptionHandler(ChannelAddException.class)
    public ResponseEntity<?> handleChannelAddException(ChannelAddException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.toResponse());
    }

    @ExceptionHandler(ChannelDeleteException.class)
    public ResponseEntity<?> handleChannelDeleteException(ChannelDeleteException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.toResponse());
    }

    @ExceptionHandler(ChannelUpdateException.class)
    public ResponseEntity<?> handleChannelUpdateException(ChannelUpdateException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.toResponse());
    }
}
