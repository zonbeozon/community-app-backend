package com.zonbeozon.reaction.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import com.zonbeozon.reaction.post.entity.ReactionType;
import com.zonbeozon.reaction.post.service.PostReactionMarker;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@RequiredArgsConstructor
public class PostReactionApi {
    private final PostReactionMarker reactionMarker;
    private final AuthenticationService authenticationService;
    private final PostAuthorizationCheckService postAuthorizationCheckService;

    @Transactional
    public void mark(Long postId, ReactionType reactionType) {
        if(!postAuthorizationCheckService.isAtLeastMember(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        reactionMarker.mark(member.getId(), postId, reactionType);
    }

    @Transactional
    public void unmark(Long postId) {
        if(!postAuthorizationCheckService.isAtLeastMember(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        reactionMarker.unmark(member.getId(), postId);
    }
}
