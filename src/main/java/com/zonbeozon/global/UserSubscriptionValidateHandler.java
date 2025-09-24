package com.zonbeozon.global;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Component
public class UserSubscriptionValidateHandler implements StompSubscriptionValidateHandler {
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private static final String USER_DESTINATION_PATTERN = "/user/**";

    @Override
    public boolean isSupport(String destination) {
        return destination != null && pathMatcher.match(USER_DESTINATION_PATTERN, destination);
    }

    @Override
    public void handle(StompHeaderAccessor accessor) {
        //noop: 이미 자체 검증한다.
    }
}
