package com.zonbeozon.global;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.service.ChannelFinder;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.global.exception.stomp.SubscriptionException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompSubscriptionValidator implements ChannelInterceptor {

    private final List<StompSubscriptionValidateHandler> handlers;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            handlers.stream()
                    .filter(handler -> handler.isSupport(destination))
                    .findAny()
                    .orElseThrow(() -> new SubscriptionException(SubscriptionException.ErrorCode.INVALID_DESTINATION))
                    .handle(accessor);
        }
        return message;
    }
}
