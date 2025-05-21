package com.zonbeozon.fiat.exception;

public class FiatException extends RuntimeException {
  public FiatException(String message) {
    super(message);
  }

  public FiatException(String message, Throwable cause) {
    super(message, cause);
  }
}
