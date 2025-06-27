package com.zonbeozon.test;

import com.zonbeozon.auth.exception.AuthException;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.exception.ChannelMemberNotFoundException;
import com.zonbeozon.channel.exception.ChannelNotFoundException;
import com.zonbeozon.channel.service.ChannelEntityQueryService;
import com.zonbeozon.channel.service.ChannelMemberEntityQueryService;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.exception.MemberNotFoundException;
import com.zonbeozon.member.service.MemberService;
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

    private final ChannelMemberEntityQueryService channelMemberEntityQueryService;
    private final ChannelEntityQueryService channelEntityQueryService;
    private final MemberService memberService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) && isChannelSubscriptionDestination(destination)) {
            Principal principal = accessor.getUser();
            if (principal == null) {
                throw new ChannelSubscriptionException(ChannelSubscriptionException.ErrorCode.UNAUTHORIZED);
            }
            Long memberId = Long.parseLong(principal.getName());
            Long channelId = extractChannelIdFromDestination(destination);

            Channel foundChannel;
            Member member;
            try {
                foundChannel = channelEntityQueryService.getChannelByIdOrThrow(channelId);
            } catch (ChannelNotFoundException e) {
                throw new ChannelSubscriptionException(ChannelSubscriptionException.ErrorCode.CHANNEL_NOT_FOUND);
            }
            try {
                member = memberService.getByIdOrThrow(memberId);
            } catch (MemberNotFoundException e) {
                throw new ChannelSubscriptionException(ChannelSubscriptionException.ErrorCode.UNAUTHORIZED);
            }
            try {
                channelMemberEntityQueryService.getChannelMemberOrThrow(member, foundChannel);
            } catch (ChannelMemberNotFoundException e) {
                throw new ChannelSubscriptionException(ChannelSubscriptionException.ErrorCode.FORBIDDEN);
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
            throw new ChannelSubscriptionException(ChannelSubscriptionException.ErrorCode.INVALID_DESTINATION);
        }
        try {
            return Long.parseLong(parts[3]);
        } catch (NumberFormatException e) {
            throw new ChannelSubscriptionException(ChannelSubscriptionException.ErrorCode.INVALID_DESTINATION);
        }
    }
}
