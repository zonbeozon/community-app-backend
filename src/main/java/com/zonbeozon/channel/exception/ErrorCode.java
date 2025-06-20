package com.zonbeozon.channel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_CHANNEL_TITLE("해당 채널 명이 이미 존재합니다."),
    ALREADY_JOINED("해당 유저는 이미 채널에 가입했습니다."),
    CHANNEL_LEAVE_NOT_ALLOWED("해당 유저는 채널을 탈퇴할 수 없습니다."),
    SAME_ROLE_CANNOT_BE_UPDATED("변경할려는 Role과 현재 Role이 같습니다"),
    CANNOT_TARGET_SELF("자기 자신을 타킷으로 할 수 없습니다.."),
    TARGET_NOT_IN_SAME_CHANNEL("요청자와 타킷이 같은 채널에 속해 있지 않습니다.");

    private final String message;

    public String getCode() {
        return this.name();
    }
}
