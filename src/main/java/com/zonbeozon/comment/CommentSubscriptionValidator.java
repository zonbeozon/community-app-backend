package com.zonbeozon.comment;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.service.finder.ChannelMemberFinder;
import com.zonbeozon.global.StompSubscriptionValidateHandler;
import com.zonbeozon.global.exception.stomp.SubscriptionException;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
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
public class CommentSubscriptionValidator implements StompSubscriptionValidateHandler {
    private static final String COMMENT_SUBSCRIPTION_PATTERN = "/topic/posts/{postId}/comments";
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final PostRepository postRepository;
    private final ChannelMemberFinder channelMemberFinder;

    @Override
    public boolean isSupport(String destination) {
        return pathMatcher.match(COMMENT_SUBSCRIPTION_PATTERN, destination);
    }

    @Override
    public void handle(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal == null) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
        Long memberId = Long.parseLong(principal.getName());
        Long postId = extractPostIdFromDestination(accessor.getDestination());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SubscriptionException(SubscriptionException.ErrorCode.POST_NOT_FOUND));
        if(post.getChannel().getSetting().getContentVisibility() == ChannelContentVisibility.PUBLIC) return;
        if(channelMemberFinder.findByChannelIdAndMemberId(post.getChannel().getId(), memberId).isPresent()) return;
        throw new SubscriptionException(SubscriptionException.ErrorCode.FORBIDDEN);
    }

    private Long extractPostIdFromDestination(String destination) {
        Map<String, String> variables = pathMatcher.extractUriTemplateVariables(COMMENT_SUBSCRIPTION_PATTERN, destination);
        String postId = variables.get("postId");
        try {
            return Long.parseLong(postId);
        } catch (NumberFormatException e) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.INVALID_DESTINATION);
        }
    }
}
