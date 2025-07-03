package com.zonbeozon.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class MemberBadRequestException extends MemberException {
  private final ErrorCode errorCode;

  public MemberBadRequestException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  @RequiredArgsConstructor
  @Getter
  public enum ErrorCode {
    DUPLICATE_USERNAME("중복 맴버 명 입니다.");

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
