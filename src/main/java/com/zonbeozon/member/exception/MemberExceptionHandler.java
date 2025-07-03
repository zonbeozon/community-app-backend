package com.zonbeozon.member.exception;

import com.zonbeozon.member.controller.MemberController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        MemberController.class
})
public class MemberExceptionHandler {
    @ExceptionHandler(MemberBadRequestException.class)
    public ResponseEntity<?> handleMemberBadRequestException(MemberBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toResponse());
    }
}
