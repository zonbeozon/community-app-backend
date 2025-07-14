package com.zonbeozon.channel.security;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.service.ChannelFinder;
import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitePublishPermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final ChannelFinder channelFinder;
    private final ChannelRoleBasedPermissionEvaluator permissionEvaluator;

    @Override
    public void handle(JoinPoint joinPoint) {
        Long channelId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.channelIdParamName, Long.class);
        Channel channel = channelFinder.findById(channelId);
        //채널 설정상 DENY라면 전부 거절
        if(channel.getSetting().getJoinPolicy() == ChannelJoinPolicy.DENY) {
            throw new ConflictException(ErrorCode.INVITE_GENERATE_DENIED);
        }
        //admin 이상이라면
        if(permissionEvaluator.hasAtLeastRole(channelId, ChannelRole.CHANNEL_ADMIN)) {
            return;
        }
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return ChannelAction.INVITE_PUBLISH == action;
    }
}
