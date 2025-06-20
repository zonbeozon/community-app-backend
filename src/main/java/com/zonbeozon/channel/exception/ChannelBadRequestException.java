package com.zonbeozon.channel.exception;


import lombok.Getter;

@Getter
public class ChannelBadRequestException extends ChannelException {
  private final String code;

  public ChannelBadRequestException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.code = errorCode.getCode();
  }
}
