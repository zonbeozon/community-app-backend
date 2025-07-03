package com.zonbeozon.channel.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ChannelBadRequestException extends ChannelException {
  private final ErrorCode errorCode;

  public ChannelBadRequestException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  @RequiredArgsConstructor
  @Getter
  public enum ErrorCode {
    DUPLICATE_CHANNEL_TITLE("해당 채널 명이 이미 존재합니다."),
    INVALID_CHANNEL_SETTING_COMBINATION("채널 설정 값들의 조합이 유효하지 않습니다."),
    ALREADY_JOINED("해당 유저는 이미 채널에 가입했습니다."),
    CHANNEL_LEAVE_NOT_ALLOWED("해당 유저는 채널을 탈퇴할 수 없습니다."),
    SAME_ROLE_CANNOT_BE_UPDATED("변경할려는 Role과 현재 Role이 같습니다"),
    CANNOT_TARGET_SELF("자기 자신을 타킷으로 할 수 없습니다."),
    TARGET_NOT_IN_SAME_CHANNEL("요청자와 타킷이 같은 채널에 속해 있지 않습니다."),
    INVITATION_CODE_INVALID("잘못된 초대 코드입니다.");

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
