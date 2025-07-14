package com.zonbeozon.channel.security;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ChannelSecurityAspect {
    public static final String channelIdParamName = "channelId";
    public static final String postIdParamName = "postId";
    public static final String targetMemberIdParamName = "targetMemberId";

    private final ChannelActionPermissionEvaluator channelActionPermissionEvaluator;

    @Before("@annotation(checkChannelAccess)")
    public void checkChannelAction(JoinPoint joinPoint, CheckChannelAccess checkChannelAccess) {
        channelActionPermissionEvaluator.evaluate(joinPoint, checkChannelAccess.value());
    }
}
