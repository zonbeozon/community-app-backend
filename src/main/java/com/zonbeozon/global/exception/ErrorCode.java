package com.zonbeozon.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("알수 없는 에러 발생."),
    //badrequest
    BAD_REQUEST("잘못된 요청입니다"),
    //authorization
    ACCESS_DENIED("접근 권한이 없습니다."),
    SAME_STATE_CANNOT_BE_UPDATED("변경할려는 상태와 현재 상태가 같습니다"),
    CANNOT_TARGET_SELF("자기자신을 타켓으로 할 수 없습니다"),

    //authentication
    UNAUTHENTICATED("인증되지 않은 사용자 입니다"),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("토큰이 만료되었습니다."),
    UNREGISTERED_OAUTH_CLIENT("등록되지 않은 OAuth 클라이언트입니다."),
    MISSING_AUTH_HEADER("Authorization 헤더가 없거나 값이 비어있습니다."),


    //channel
    CHANNEL_JOIN_DENIED("채널의 가입 정책에 의해 접근이 거부되었습니다"),
    INVALID_INVITE_CODE("잘못된 초대 코드입니다."),
    CHANNEL_LEAVE_NOT_ALLOWED("채널을 탈퇴할 수 없습니다"),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다"),
    CHANNEL_MEMBER_NOT_FOUND("채널에 가입하지 않았습니다."),
    DUPLICATE_CHANNEL_TITLE("해당 채널 명이 이미 존재합니다."),
    CHANNEL_NAME_ALREADY_EXISTS("이미 존재하는 채널 이름입니다."),
    KICKED_MEMBER_CANNOT_JOIN("강퇴당한 사용자는 재가입이 불가능 합니다."),
    ALREADY_JOINED_CHANNEL("이미 참가한 채널입니다."),
    INVITE_GENERATE_DENIED("채널의 가입 정책에 따라 초대코드를 생성할 수 없습니다."),
    NOT_INFO_CHANNEL("INFO채널이 아닙니다."),


    //member
    MEMBER_NOT_FOUND("맴버를 찾을 수 없습니다"),

    //post
    POST_NOT_FOUND("post를 찾을 수 없습니다"),

    //reaction
    REACTION_NOT_FOUND("해당 reaction 기록이 없습니다");

    private final String message;
}
