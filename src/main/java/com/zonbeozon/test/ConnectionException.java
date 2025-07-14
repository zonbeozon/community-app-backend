package com.zonbeozon.test;

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
    UNAUTHORIZED("인증 정보가 없거나 유효하지 않습니다.");

    private final String message;
  }
}
