package com.zonbeozon.global.exception.stomp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SubscriptionException extends RuntimeException {
    private final ErrorCode errorCode;

    public SubscriptionException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @AllArgsConstructor
    @Getter
    public enum ErrorCode {
        UNAUTHORIZED("인증 정보가 없거나 유효하지 않습니다."),
        FORBIDDEN("접근할 권한이 없습니다."),
        INVALID_DESTINATION("잘못된 구독 주소입니다."),
        CHANNEL_NOT_FOUND("존재하지 않는 채널입니다.");

        private final String message;
    }
}
