package com.zonbeozon.integral.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@TestConfiguration
public class ReceiptAckConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    @Lazy //순환 루프 방지
    private MessageChannel clientOutboundChannel;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ExecutorChannelInterceptor() {
            @Override
            public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
                StompHeaderAccessor inAccessor = StompHeaderAccessor.wrap(message);
                String receipt = inAccessor.getReceipt();
                if(!StringUtils.hasText(receipt)) {
                    return;
                }
                StompHeaderAccessor outAccessor = StompHeaderAccessor.create(StompCommand.RECEIPT);
                outAccessor.setReceiptId(receipt);
                outAccessor.setSessionId(inAccessor.getSessionId());
                Message<byte[]> outMessage = MessageBuilder.createMessage(new byte[0], outAccessor.getMessageHeaders());
                clientOutboundChannel.send(outMessage);
            }
        });
    }
}
