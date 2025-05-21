package com.zonbeozon.test;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

public class StompExceptionHandler extends StompSubProtocolErrorHandler {
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        String errorMsg = ex.getMessage();
        byte[] payload = errorMsg.getBytes(StandardCharsets.UTF_8);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        return super.handleClientMessageProcessingError(
                MessageBuilder.createMessage(payload, accessor.getMessageHeaders()),
                ex
        );
    }
}
