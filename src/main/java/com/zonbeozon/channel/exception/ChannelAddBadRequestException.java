package com.zonbeozon.channel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ChannelAddBadRequestException extends ChannelException {
    private final ErrorCode errorCode;

    public ChannelAddBadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ErrorCode {
        DUPLICATE_CHANNEL_TITLE("해당 채널 명이 이미 존재합니다.");
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
