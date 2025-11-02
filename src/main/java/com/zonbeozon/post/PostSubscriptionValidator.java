package com.zonbeozon.post;

import com.zonbeozon.channel.service.finder.BlogChannelFinder;
import com.zonbeozon.channel.service.finder.ChannelMemberFinder;
import com.zonbeozon.global.StompSubscriptionValidateHandler;
import com.zonbeozon.global.exception.stomp.SubscriptionException;
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
public class PostSubscriptionValidator implements StompSubscriptionValidateHandler {
    private static final String POST_SUBSCRIPTION_PATTERN = "/topic/channel/{channelId}/post";
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final BlogChannelFinder blogChannelFinder;
    private final ChannelMemberFinder channelMemberFinder;

    @Override
    public boolean isSupport(String destination) {
        return pathMatcher.match(POST_SUBSCRIPTION_PATTERN, destination);
    }

    @Override
    public void handle(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal == null) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
        Long memberId = Long.parseLong(principal.getName());
        Long channelId = extractChannelIdFromDestination(accessor.getDestination());

        if(!blogChannelFinder.existsById(channelId))
            throw new SubscriptionException(SubscriptionException.ErrorCode.CHANNEL_NOT_FOUND);
        if(!channelMemberFinder.existsByChannelIdAndMemberId(channelId, memberId))
            throw new SubscriptionException(SubscriptionException.ErrorCode.FORBIDDEN);
    }

    private Long extractChannelIdFromDestination(String destination) {
        Map<String, String> variables = pathMatcher.extractUriTemplateVariables(POST_SUBSCRIPTION_PATTERN, destination);
        String channelId = variables.get("channelId");
        try {
            return Long.parseLong(channelId);
        } catch (NumberFormatException e) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.INVALID_DESTINATION);
        }
    }
}
