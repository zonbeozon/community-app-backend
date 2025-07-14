package com.zonbeozon.test;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.service.ChannelFinder;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class StompChannelSubscribeAuthorizationInterceptor implements ChannelInterceptor {

    private final ChannelMemberFinder channelMemberFinder;
    private final ChannelFinder channelFinder;
    private final MemberFinder memberFinder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) && isChannelSubscriptionDestination(destination)) {
            Principal principal = accessor.getUser();
            if (principal == null) {
                throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
            }
            Long memberId = Long.parseLong(principal.getName());
            Long channelId = extractChannelIdFromDestination(destination);

            Channel foundChannel;
            Member member;
            try {
                foundChannel = channelFinder.findById(channelId);
            } catch (NotFoundException e) {
                throw new SubscriptionException(SubscriptionException.ErrorCode.CHANNEL_NOT_FOUND);
            }
            try {
                member = memberFinder.findById(memberId);
            } catch (NotFoundException e) {
                throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
            }
            try {
                channelMemberFinder.findByMemberAndChannel(member, foundChannel);
            } catch (NotFoundException e) {
                throw new SubscriptionException(SubscriptionException.ErrorCode.FORBIDDEN);
            }
        }
        return message;
    }

    private boolean isChannelSubscriptionDestination(String destination) {
        return destination != null && destination.startsWith("/topic/channel/");
    }

    private Long extractChannelIdFromDestination(String destination) {
        // 예: /topic/channel/123 → 123
        String[] parts = destination.split("/");
        if (parts.length < 4) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.INVALID_DESTINATION);
        }
        try {
            return Long.parseLong(parts[3]);
        } catch (NumberFormatException e) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.INVALID_DESTINATION);
        }
    }
}
