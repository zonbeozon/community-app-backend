package com.zonbeozon.reaction.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import com.zonbeozon.reaction.enums.ReactionContentType;
import com.zonbeozon.reaction.enums.ReactionType;
import com.zonbeozon.reaction.service.ReactionMarker;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@RequiredArgsConstructor
@Transactional
public class PostReactionApi {
    private final ReactionMarker reactionMarker;
    private final AuthenticationService authenticationService;
    private final PostAuthorizationCheckService postAuthorizationCheckService;

    public void mark(Long postId, ReactionContentType contentType, ReactionType reactionType) {
        if(!postAuthorizationCheckService.isAtLeastMember(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        reactionMarker.mark(member.getId(), postId, contentType, reactionType);
    }

    public void unmark(Long postId, ReactionContentType contentType) {
        if(!postAuthorizationCheckService.isAtLeastMember(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        reactionMarker.unmark(member.getId(), postId, contentType);
    }
}
