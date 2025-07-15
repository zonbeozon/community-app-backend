package com.zonbeozon.post.security;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.security.*;
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
    private final SimpleChannelPermissionEvaluator permissionEvaluator;
    private final PostFinder postFinder;
    private final AuthenticationService authenticationService;

    @Override
    public void handle(JoinPoint joinPoint) {
        Member requester = authenticationService.getCurrentMember();
        Long postId = AspectUtils.extractParameter(joinPoint, ChannelSecurityAspect.postIdParamName, Long.class);
        Post post = postFinder.findById(postId);
        Member author = post.getAuthor();
        if(
                //채널에 가입되어있고
                permissionEvaluator.isMemberOfChannel(post.getChannel().getId())
                //작성자거나 작성자 보다 권한이 높다면
                && (author.equals(requester) || permissionEvaluator.isSuperiorTo(post.getChannel().getId(), author))
        ) return;
        //나머지 경우라면 권한 없음
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return ChannelAction.POST_DELETE == action;
    }
}
