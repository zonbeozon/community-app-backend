package com.zonbeozon.channel.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChannelAction {
    CREATION("채널 생성"),
    INFO_MODIFICATION("채널 정보 수정"),
    DELETION("채널 삭제"),
    MEMBER_PROMOTION("유저 권한 프로모션"),
    MEMBER_DEMOTION("유저 권한 디모션"),
    MEMBER_KICK("유저 강제 퇴장"),
    JOIN("채널 참가"),
    INVITE_JOIN("초대를 통한 채널 참가"),
    INVITE("채널 초대");

    private final String krDesc;
}
