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
        //작성자가 채널에 가입되어 있다면
        if(permissionEvaluator.isMemberOfChannel(post.getChannel().getId(), author)) {
            //요청자가 채널에 가입되어 있고 작성자보다 권한이 높거나 작성자라면
            if(
                    permissionEvaluator.isMemberOfChannel(post.getChannel().getId())
                    && (author.equals(requester) || permissionEvaluator.isSuperiorTo(post.getChannel().getId(), author))
            ) return;
        }
        //작성자가 채널에 가입되어있지 않은 상태라면
        else {
            //요청자가 채널에 가입되어 있고 Admin 이상이라면
            if(
                    permissionEvaluator.isMemberOfChannel(post.getChannel().getId())
                    && permissionEvaluator.hasMinimumRole(post.getChannel().getId(), ChannelRole.CHANNEL_ADMIN)
            ) return;
        }
        //나머지 경우라면 권한 없음
        throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public boolean isSupport(ChannelAction action) {
        return ChannelAction.POST_DELETE == action;
    }
}
