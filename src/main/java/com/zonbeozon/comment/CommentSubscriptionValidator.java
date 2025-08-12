package com.zonbeozon.comment;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.service.ChannelFinder;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.global.StompSubscriptionValidateHandler;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.global.exception.stomp.SubscriptionException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostFinder;
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
    private static final String COMMENT_SUBSCRIPTION_PATTERN = "/topic/post/{postId}/comment";
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final MemberFinder memberFinder;
    private final ChannelMemberFinder channelMemberFinder;
    private final PostFinder postFinder;

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

        Post post;
        Channel channel;
        Member member;
        try {
            post = postFinder.findById(postId);
        } catch (NotFoundException e) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.POST_NOT_FOUND);
        }
        channel = post.getChannel();
        try {
            member = memberFinder.findById(memberId);
        } catch (NotFoundException e) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.UNAUTHORIZED);
        }
        try {
            channelMemberFinder.findByMemberAndChannel(member, channel);
        } catch (NotFoundException e) {
            throw new SubscriptionException(SubscriptionException.ErrorCode.FORBIDDEN);
        }
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
