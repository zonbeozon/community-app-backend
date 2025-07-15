package com.zonbeozon.channel.security;

import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelActionPermissionEvaluator {
    private final List<ChannelActionPermissionEvaluateHandler> handlers;
    private final SimpleChannelPermissionEvaluator permissionEvaluator;

    public void evaluateChannelAction(JoinPoint joinPoint, ChannelAction action) {
       handlers.stream()
               .filter(handler -> handler.isSupport(action))
               .findAny()
               .orElseThrow(() -> new IllegalArgumentException("해당 action을 지원하는 헨들러가 등록되지 않았습니다"))
               .handle(joinPoint);
    }

    public void evaluateMemberJoined(JoinPoint joinPoint) {
        Long channelId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.channelIdParamName, Long.class);
        if(permissionEvaluator.isMemberOfChannel(channelId)) return;
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

}
