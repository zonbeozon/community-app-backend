package com.zonbeozon.channel.exception;

import com.zonbeozon.channel.controller.ChannelController;
import com.zonbeozon.channel.controller.ChannelMemberController;
import com.zonbeozon.post.controller.PostController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {
        ChannelController.class,
        ChannelMemberController.class,
        PostController.class,
})
public class ChannelExceptionHandler {
    @ExceptionHandler(ChannelAccessDeniedException.class)
    public ResponseEntity<String> handleChannelAccessDeniedException(ChannelAccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }
}
