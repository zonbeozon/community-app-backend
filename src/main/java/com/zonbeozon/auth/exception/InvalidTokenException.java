package com.zonbeozon.auth.exception;

public class InvalidTokenException extends AuthException {
    public static final String DEFAULT_MESSAGE = "잘못된 토큰입니다. 다시 로그인 해주세요";
    public static final String EXPIRED_MESSAGE = "토큰이 만료되었습니다, /auth/reissue 엔드포인트를 통해서 다시 발급해 주세요.";
    public InvalidTokenException(String message) {
        super(message);
    }
    public InvalidTokenException() {
        super(DEFAULT_MESSAGE);
    }
}
