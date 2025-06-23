package com.zonbeozon.channel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class ChannelAddException extends ChannelException {
    private final ErrorCode errorCode;

    public ChannelAddException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ErrorCode {
        DUPLICATE_CHANNEL_TITLE(HttpStatus.BAD_REQUEST, "해당 채널 명이 이미 존재합니다."),
        ACCESS_DENIED(HttpStatus.FORBIDDEN,"해당 채널을 만들 권한이 없습니다."),
        OPEN_LEVEL_MISMATCH(HttpStatus.BAD_REQUEST, "검색 가능 여부가 비공개라면 열람 설정은 비공개만 허용됩니다.");

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
