package com.zonbeozon.channel.security;

import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class KickedChannelMemberReadPermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final SimpleChannelPermissionEvaluator permissionEvaluator;

    @Override
    public void handle(JoinPoint joinPoint) {
        Long channelId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.channelIdParamName, Long.class);
        if(permissionEvaluator.isMemberOfChannel(channelId) && permissionEvaluator.hasMinimumRole(channelId, ChannelRole.CHANNEL_OWNER)) {
            return;
        }
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return action == ChannelAction.READ_KICKED_MEMBER;
    }
}
