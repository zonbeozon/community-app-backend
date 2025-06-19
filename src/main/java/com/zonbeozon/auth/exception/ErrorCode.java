package com.zonbeozon.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"토큰이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"refresh 토큰이 만료되었습니다, 재 로그인이 필요합니다"),
    UNREGISTERED_OAUTH_CLIENT(HttpStatus.UNAUTHORIZED,"등록되지 않은 OAuth 클라이언트입니다."),
    MISSING_AUTH_HEADER(HttpStatus.UNAUTHORIZED, "Authorization 헤더가 없거나 값이 비어있습니다.");;

    private final HttpStatus httpStatus;
    private final String message;

    public String getErrorCode() {
        return this.name();
    }
}
