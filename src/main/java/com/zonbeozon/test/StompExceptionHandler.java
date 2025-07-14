package com.zonbeozon.test;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper; // Jackson ObjectMapper 임포트

@Slf4j
public class StompExceptionHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 인스턴스 생성

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        // StompErrorResponse 객체 생성
        ErrorResponse errorResponse = switch (ex) {
            case SubscriptionException subscriptionException -> new ErrorResponse(
                    subscriptionException.getErrorCode().name(),
                    subscriptionException.getErrorCode().getMessage()
            );
            case ConnectionException connectionException -> new ErrorResponse(
                    connectionException.getErrorCode().name(),
                    connectionException.getErrorCode().getMessage()
            );
            default -> {
                log.error(ex.getMessage(), ex);
                yield new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.name(),  ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
            }
        };

        byte[] payload;
        try {
            payload = objectMapper.writeValueAsBytes(errorResponse);
        } catch (Exception jsonEx) {
            log.error("Failed to serialize STOMP error response.", jsonEx);
            // 클라이언트에게는 최대한 일반적인 내부 서버 오류 메시지를 전달
            payload = ("{\"code\":\"" +
                    ErrorCode.INTERNAL_SERVER_ERROR.name() +
                    "\",\"message\":\"" + ErrorCode.INTERNAL_SERVER_ERROR.getMessage() +
                    "\"}")
                    .getBytes(StandardCharsets.UTF_8);
        }

        // STOMP 헤더 설정
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);
        accessor.setNativeHeader("content-type", "application/json;charset=UTF-8");

        return super.handleClientMessageProcessingError(
                MessageBuilder.createMessage(payload, accessor.getMessageHeaders()),
                ex // 원본 예외 전달
        );
    }
}
