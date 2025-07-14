package com.zonbeozon.channel.security;

import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KickPermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final ChannelRoleBasedPermissionEvaluator permissionEvaluator;
    private final MemberFinder memberFinder;
    /**
     * 대상 보다 권한이 높다면 허용
     */
    @Override
    public void handle(JoinPoint joinPoint) {
        Long channelId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.channelIdParamName, Long.class);
        Long targetMemberId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.targetMemberIdParamName, Long.class);
        Member targetMember = memberFinder.findById(targetMemberId);
        //대상보다 궎한이 높다면
        if(permissionEvaluator.isSuperiorTo(channelId, targetMember)) {
            return;
        }
        //낮거나 같다면
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return ChannelAction.KICK == action;
    }
}
