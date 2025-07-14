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
public class PostDeletePermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final ChannelRoleBasedPermissionEvaluator permissionEvaluator;
    private final PostFinder postFinder;
    private final AuthenticationService authenticationService;

    @Override
    public void handle(JoinPoint joinPoint) {
        Member requester = authenticationService.getCurrentMember();
        Long postId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.postIdParamName, Long.class);
        Post post = postFinder.findById(postId);
        Member author = post.getAuthor();
        //채널 활성 맴버인지 체크
        permissionEvaluator.hasAtLeastRole(post.getChannel().getId(), ChannelRole.CHANNEL_MEMBER);
        //작성자라면
        if(author.equals(requester)) return;
        //작성자보다 권한이 높다면
        if(permissionEvaluator.isSuperiorTo(post.getChannel().getId(), author)) return;
        //나머지 경우라면 권한 없음
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return ChannelAction.POST_DELETE == action;
    }
}
