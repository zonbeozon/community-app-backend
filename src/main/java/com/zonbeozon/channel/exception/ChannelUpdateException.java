package com.zonbeozon.channel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class ChannelUpdateException extends ChannelException {
  private final ErrorCode errorCode;

  public ChannelUpdateException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  @RequiredArgsConstructor
  @Getter
  public enum ErrorCode {
    DUPLICATE_CHANNEL_TITLE(HttpStatus.BAD_REQUEST, "해당 채널 명이 이미 존재합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN,"해당 채널을 업데이트할 권한이 없습니다."),
    INVALID_CHANNEL_SETTING_COMBINATION(HttpStatus.BAD_REQUEST, "채널 설정 값들의 조합이 유효하지 않습니다.");

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
