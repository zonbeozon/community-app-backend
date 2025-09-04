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
import java.util.Map;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelMemberSubscriptionValidator implements StompSubscriptionValidateHandler {
    private static final String MEMBER_SUBSCRIPTION_PATTERN = "/topic/member/{memberId}/channel";
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final MemberFinder memberFinder;

    @Override
    public boolean isSupport(String destination) {
        return pathMatcher.match(MEMBER_SUBSCRIPTION_PATTERN, destination);
    }

    @Override
    public void handle(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal == null) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
        Long memberId = Long.parseLong(principal.getName());

        if(!memberId.equals(extractMemberIdFromDestination(accessor.getDestination()))) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
        if(!memberFinder.existsById(memberId)) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
    }
    private Long extractMemberIdFromDestination(String destination) {
        Map<String, String> variables = pathMatcher.extractUriTemplateVariables(MEMBER_SUBSCRIPTION_PATTERN, destination);
        String memberId = variables.get("memberId");
        try {
            return Long.parseLong(memberId);
        } catch (NumberFormatException e) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.INVALID_DESTINATION);
        }
    }

}
