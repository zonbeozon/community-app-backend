package com.zonbeozon.chat;

import com.zonbeozon.chat.service.ChattingGroupFinder;
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
public class ChatSubscriptionValidator implements StompSubscriptionValidateHandler {
    private static final String CHAT_SUBSCRIPTION_PATTERN = "/topic/chatting-groups/{chattingGroupId}/chats";
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final ChattingGroupFinder chattingGroupFinder;

    @Override
    public boolean isSupport(String destination) {
        return pathMatcher.match(CHAT_SUBSCRIPTION_PATTERN, destination);
    }

    @Override
    public void handle(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal == null) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
        Long chattingGroupId = extractGroupNameFromDestination(accessor.getDestination());
        chattingGroupFinder.findById(chattingGroupId)
                .orElseThrow(() -> new SubscriptionException(SubscriptionException.ErrorCode.CHATTING_GROUP_NOT_FOUND));
    }

    private Long extractGroupNameFromDestination(String destination) {
        Map<String, String> variables = pathMatcher.extractUriTemplateVariables(CHAT_SUBSCRIPTION_PATTERN, destination);
        return Long.parseLong(variables.get("chattingGroupId"));
    }
}
