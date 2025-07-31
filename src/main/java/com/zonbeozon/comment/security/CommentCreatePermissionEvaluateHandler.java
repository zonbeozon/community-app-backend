package com.zonbeozon.comment.security;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.ChannelActionPermissionEvaluateHandler;
import com.zonbeozon.channel.security.ChannelSecurityAspect;
import com.zonbeozon.channel.security.SimpleChannelPermissionEvaluator;
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

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class CommentCreatePermissionEvaluateHandler implements ChannelActionPermissionEvaluateHandler {
    private final SimpleChannelPermissionEvaluator permissionEvaluator;
    private final PostFinder postFinder;
    private final AuthenticationService authenticationService;

    @Override
    public void handle(JoinPoint joinPoint) {
        Member requester = authenticationService.getCurrentMember();
        Long postId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.postIdParamName, Long.class);
        Post post = postFinder.findById(postId);
        //요청자가 채널에 가입되어있지 않다면
        if(!permissionEvaluator.isMemberOfChannel(post.getChannel().getId(), requester))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return action == ChannelAction.COMMENT_CREATE;
    }
}
