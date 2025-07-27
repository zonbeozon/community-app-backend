package com.zonbeozon.global.exception.stomp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ConnectionException extends RuntimeException {
  private final ErrorCode errorCode;

  public ConnectionException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.errorCode = errorCode;
  }

  @AllArgsConstructor
  @Getter
  public enum ErrorCode {
    UNAUTHENTICATED("인증 정보가 없거나 유효하지 않습니다."),
    EXPIRED_TOKEN("토큰이 만료되었습니다. 재발급이 필요합니다");

    private final String message;
  }
}
