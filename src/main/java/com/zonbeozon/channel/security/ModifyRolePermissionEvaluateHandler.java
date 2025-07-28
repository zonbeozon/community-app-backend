package com.zonbeozon.channel.security;

import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModifyRolePermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final MemberFinder memberFinder;
    private final SimpleChannelPermissionEvaluator permissionEvaluator;

    @Override
    public void handle(JoinPoint joinPoint) {
        Long channelId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.channelIdParamName, Long.class);
        Long targetMemberId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.targetMemberIdParamName, Long.class);
        Member targetMember = memberFinder.findById(targetMemberId);
        //요청자가 채널에 가입되어있고 대상보다 권한이 높다면
        if(permissionEvaluator.isMemberOfChannel(channelId) && permissionEvaluator.isSuperiorTo(channelId, targetMember)) {
            return;
        }
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);

    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return action == ChannelAction.MODIFY_ROLE;
    }
}
