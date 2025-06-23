package com.zonbeozon.channel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class ChannelDeleteException extends RuntimeException {
    private final ErrorCode errorCode;

    public ChannelDeleteException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ErrorCode {
        ACCESS_DENIED(HttpStatus.FORBIDDEN,"해당 채널을 삭제할 권한이 없습니다.");

        private final HttpStatus httpStatus;
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
