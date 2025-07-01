package com.zonbeozon.channel.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class ChannelAccessDeniedException extends ChannelException {
    public static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;
    private final ErrorCode errorCode;

    public ChannelAccessDeniedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ErrorCode {
        CHANNEL_CREATION_FORBIDDEN("해당 채널을 만들 권한이 없습니다."),
        KICK_FORBIDDEN("강퇴시키려는 맴버보다 권한이 높아야 합니다."),
        INVITE_FORBIDDEN("채널 초대 권한이 없습니다."),
        INVITE_FORBIDDEN_BY_CHANNEL_SETTING("채널 초대는 해당 채널에서 막힌 상태입니다."),
        JOIN_FORBIDDEN("공개 가입 채널이 아닙니다."),
        CONTENT_READ_FORBIDDEN("채널 컨텐츠 접근 권한이 없습니다"),
        MODIFY_CHANNEL_ROLE_FORBIDDEN("채널 멤버의 권한을 수정할 수 있는 권한이 없습니다."),
        MODIFY_CHANNEL_METADATA_FORBIDDEN("채널 메타데이터를 변경할 권한이 없습니다."),
        KICKED_MEMBER_CANNOT_JOIN_DIRECTLY("강퇴당한 멤버는 초대를 통해서만 재가입할 수 있습니다."),
        CHANNEL_DELETION_FORBIDDEN("해당 채널을 삭제할 권한이 없습니다."),
        CHANNEL_MEMBER_MISMATCH("요청한 맴버는 해당 채널에 속해있지 않습니다.");;

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
