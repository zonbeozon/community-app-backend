package com.zonbeozon.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PostAccessDeniedException extends PostException {
    private final ErrorCode errorCode;

    public PostAccessDeniedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ErrorCode {
        POST_CREATION_DENIED("post 작성 권한이 없습니다."),
        POST_DELETION_DENIED("post 삭제 권한이 없습니다."),
        POST_UPDATE_DENIED("post 업데이트 권한이 없습니다.");

        private final String message;
    }

    public record Response(
            ErrorCode errorCode,
            String message
    ) {
    }
    public Response toResponse() {
        return new Response(this.errorCode, this.getMessage());
    }
}
