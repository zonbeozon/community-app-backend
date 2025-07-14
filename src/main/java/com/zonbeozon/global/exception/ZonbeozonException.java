package com.zonbeozon.global.exception;

public class ZonbeozonException extends RuntimeException {
  private final ErrorCode errorCode;
  public ZonbeozonException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode.name();
  }
}
