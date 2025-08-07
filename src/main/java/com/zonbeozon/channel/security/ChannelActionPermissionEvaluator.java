package com.zonbeozon.channel.security;

import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.post.service.PostFinder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelActionPermissionEvaluator {
    private final List<ChannelActionPermissionEvaluateHandler> handlers;
    private final SimpleChannelPermissionEvaluator permissionEvaluator;
    private final PostFinder postFinder;

    public void evaluateChannelAction(JoinPoint joinPoint, ChannelAction action) {
       handlers.stream()
               .filter(handler -> handler.isSupport(action))
               .findAny()
               .orElseThrow(() -> new IllegalArgumentException("해당 action을 지원하는 헨들러가 등록되지 않았습니다"))
               .handle(joinPoint);
    }

    public void evaluateMemberJoined(JoinPoint joinPoint, String evaluateBy) {
        Long channelId;
        if(evaluateBy.equals(ChannelSecurityAspect.channelIdParamName)) {
            channelId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.channelIdParamName, Long.class);
        }
        else if(evaluateBy.equals(ChannelSecurityAspect.postIdParamName)) {
            Long postId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.postIdParamName, Long.class);
            channelId = postFinder.findById(postId).getChannel().getId();
        }
        else {
            throw new IllegalArgumentException("해당 조건은 지원하지 않습니다.");
        }
        if(permissionEvaluator.isMemberOfChannel(channelId)) return;
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

}
