package com.zonbeozon.post.security;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.InfoChannel;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.ChannelActionPermissionEvaluateHandler;
import com.zonbeozon.channel.security.ChannelSecurityAspect;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.channel.service.InfoChannelFinder;
import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReadPermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final InfoChannelFinder infoChannelFinder;
    private final AuthenticationService authenticationService;
    private final ChannelMemberFinder channelMemberFinder;

    @Override
    public void handle(JoinPoint joinPoint) {
        Long channelId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.channelIdParamName, Long.class);
        InfoChannel channel = infoChannelFinder.findById(channelId);
        //컨텐츠 읽기가 PUBLIC이라면 채널 참가 여부와 무관하게 허용
        if(channel.getSetting().getContentVisibility() == ChannelContentVisibility.PUBLIC) return;

        //컨텐츠 읽기가 Member Only라면 채널 참가 여부 확인
        Member requester = authenticationService.getCurrentMember();
        try {
            channelMemberFinder.findByMemberAndChannel(requester, channel);
        } catch (NotFoundException e) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return ChannelAction.POST_READ == action;
    }
}
