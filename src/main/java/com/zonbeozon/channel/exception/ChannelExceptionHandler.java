package com.zonbeozon.channel.exception;

import com.zonbeozon.channel.controller.ChannelController;
import com.zonbeozon.post.controller.PostController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        ChannelController.class,
        PostController.class
})
public class ChannelExceptionHandler {
    @ExceptionHandler(ChannelBadRequestException.class)
    public ResponseEntity<?> handleChannelBadRequestException(ChannelBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toResponse());
    }

    @ExceptionHandler(ChannelAccessDeniedException.class)
    public ResponseEntity<?> handleChannelAccessDeniedException(ChannelAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toResponse());
    }

    @ExceptionHandler(ChannelMemberNotFoundException.class)
    public ResponseEntity<?> handleChannelMemberNotFoundException(ChannelMemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<?> handleChannelNotFoundException(ChannelNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
