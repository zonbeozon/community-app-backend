package com.zonbeozon.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    //commons
    INVERTED_SEARCH_REQUIRES_CURSOR("역방향(inverted) 조회 시에는 기준이 되는 커서 값(createdAt, postId)이 반드시 필요합니다."),
    METHOD_NOT_ALLOWED("지원하지 않는 Http Method입니다."),
    INTERNAL_SERVER_ERROR("알수 없는 에러 발생."),
    SERVICE_NOT_READY("아직 제공되지 않는 서비스입니다."),
    DATA_IS_STALE("내부 문제로 인해 최신 데이터를 제공할 수 없습니다."),
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
    BANNED_MEMBER_CANNOT_JOIN("강퇴당한 사용자는 재가입이 불가능 합니다."),
    ALREADY_JOINED_CHANNEL("이미 참가했거나 참가 신청을 한 채널입니다."),
    INVITE_GENERATE_DENIED("채널의 가입 정책에 따라 초대코드를 생성할 수 없습니다."),
    OPERATION_FOR_BLOG_CHANNEL_ONLY("블로그 채널 전용 작업입니다."),
    CHANNEL_MEMBER_IS_NOT_PENDING_STATUS("승인 대기중인 맴버가 아닙니다"),
    CHANNEL_MEMBER_IS_NOT_BAN_STATUS("해당 채널 맴버는 BAN 상태가 아닙니다."),

    //member
    MEMBER_NOT_FOUND("맴버를 찾을 수 없습니다"),
    DUPLICATE_USERNAME("중복된 유저명입니다"),
    DUPLICATE_EMAIL("중복된 이메일입니다"),



    //post
    POST_NOT_FOUND("post를 찾을 수 없습니다"),
    MAX_POST_IMAGE_REACHED("Post 이미지 최대 업로드 개수를 초과했습니다"),
    INVALID_POST_CURSOR_COMBINATION("createdAt과 postId는 함께 제공되거나 함께 생략되어야 합니다."),

    //reaction
    REACTION_NOT_FOUND("해당 reaction 기록이 없습니다"),

    //image
    IMAGE_NOT_FOUND("해당 조건에 맞는 image가 존재하지 않습니다."),

    //comment
    COMMENT_NOT_FOUND("해당 조건에 맞는 comment가 존재하지 않습니다."),

    //coinInfo
    SYMBOL_NOT_FOUND("존재하지 않는 코인 심볼입니다."),
    UNSUPPORTED_BASE_ASSET("지원하지 않는 통화입니다"),
    UNSUPPORTED_LANGUAGE("지원하지 않는 언어입니다.");

    private final String message;
}
