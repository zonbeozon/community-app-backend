package com.zonbeozon.global.exception.stomp;

import com.zonbeozon.global.ObjectResponseToByteConverter;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.dto.ErrorResponse;
import com.zonbeozon.global.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompExceptionHandler extends StompSubProtocolErrorHandler {
    private final ObjectResponseToByteConverter objectResponseToByteConverter;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {

        ErrorResponse errorResponse;

        Optional<Throwable> optSubEx = ExceptionUtils.getCause(ex, SubscriptionException.class);
        Optional<Throwable> optConEx = ExceptionUtils.getCause(ex, ConnectionException.class);

        if (optSubEx.isPresent()) {
            SubscriptionException subscriptionException = (SubscriptionException) optSubEx.get();
            errorResponse = new ErrorResponse(subscriptionException.getErrorCode().name(), subscriptionException.getMessage());
        } else if (optConEx.isPresent()) {
            ConnectionException connectionException = (ConnectionException) optConEx.get();
            errorResponse = new ErrorResponse(connectionException.getErrorCode().name(), connectionException.getMessage());
        } else {
            log.error("알수 없는 예외 발생: {}", ex.getMessage());
            errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.name(),  ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }

        byte[] payload = objectResponseToByteConverter.convert(errorResponse);

        // STOMP 헤더 설정
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);
        accessor.setNativeHeader("content-type", "application/json;charset=UTF-8");

        return MessageBuilder.createMessage(payload, accessor.getMessageHeaders());
    }
}
