package com.zonbeozon.comment.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.comment.dto.CommentAddRequest;
import com.zonbeozon.comment.service.CommentAuthorizationCheckService;
import com.zonbeozon.comment.service.CommentCreator;
import com.zonbeozon.comment.service.CommentDeleteService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import lombok.RequiredArgsConstructor;

@ApiComponent
@RequiredArgsConstructor
public class CommentCommandApi {
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final CommentAuthorizationCheckService commentAuthorizationCheckService;
    private final CommentDeleteService commentDeleteService;
    private final AuthenticationService authenticationService;
    private final CommentCreator commentCreator;

    public Long createComment(Long postId, CommentAddRequest request) {
        if(!postAuthorizationCheckService.isAtLeastMember(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        return commentCreator.addComment(member.getId(), postId, request.content());
    }

    public void deleteComment(Long commentId) {
        if(!commentAuthorizationCheckService.isAuthorOrHasHigherRoleThanAuthor(commentId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        commentDeleteService.delete(commentId);
    }
}
