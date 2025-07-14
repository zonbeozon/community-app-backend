package com.zonbeozon.post.security;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.ChannelActionPermissionEvaluateHandler;
import com.zonbeozon.channel.security.ChannelRoleBasedPermissionEvaluator;
import com.zonbeozon.channel.security.ChannelSecurityAspect;
import com.zonbeozon.global.AspectUtils;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostFinder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostUpdatePermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final AuthenticationService authenticationService;
    private final PostFinder postFinder;
    private final ChannelRoleBasedPermissionEvaluator permissionEvaluator;
    /**
     * 작성자만 허용
     */
    @Override
    public void handle(JoinPoint joinPoint) {
        Member requester = authenticationService.getCurrentMember();
        Long postId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.postIdParamName, Long.class);
        Post post = postFinder.findById(postId);
        //채널 활성 맴버인지 체크
        permissionEvaluator.hasAtLeastRole(post.getChannel().getId(), ChannelRole.CHANNEL_MEMBER);
        Member author = post.getAuthor();
        //작성자라면
        if(author.equals(requester)) return;
        //작성자가 아니라면
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return ChannelAction.POST_UPDATE == action;
    }
}
