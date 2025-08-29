package com.zonbeozon.channel;

import com.zonbeozon.global.StompSubscriptionValidateHandler;
import com.zonbeozon.global.exception.stomp.SubscriptionException;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelMemberSubscriptionValidator implements StompSubscriptionValidateHandler {
    private static final String CHANNEL_SUBSCRIPTION_PATTERN = "/topic/member/channel";
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final MemberFinder memberFinder;

    @Override
    public boolean isSupport(String destination) {
        return pathMatcher.match(CHANNEL_SUBSCRIPTION_PATTERN, destination);
    }

    @Override
    public void handle(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal == null) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
        Long memberId = Long.parseLong(principal.getName());
        if(!memberFinder.existsById(memberId)) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
    }
}
