package com.zonbeozon.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class PostBadRequestException extends RuntimeException {
  public static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
  private final ErrorCode errorCode;

  public PostBadRequestException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  @RequiredArgsConstructor
  @Getter
  public enum ErrorCode {
    POST_NOT_SUPPORTED_CHANNEL("post를 작성할 수 없는 채널입니다.");

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
