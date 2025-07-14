package com.zonbeozon.post.security;

import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.ChannelActionPermissionEvaluateHandler;
import com.zonbeozon.channel.security.ChannelRoleBasedPermissionEvaluator;
import com.zonbeozon.channel.security.ChannelSecurityAspect;
import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCreatePermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final ChannelRoleBasedPermissionEvaluator permissionEvaluator;
    /**
     * channel admin 이상 부터 가능
     */
    @Override
    public void handle(JoinPoint joinPoint) {
        Long channelId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.channelIdParamName, Long.class);
        //admin 이상이라면
        if(permissionEvaluator.hasAtLeastRole(channelId, ChannelRole.CHANNEL_ADMIN)) return;
        //나머지 경우에는 허용 안된다.
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);

    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return ChannelAction.POST_CREATE == action;
    }
}
