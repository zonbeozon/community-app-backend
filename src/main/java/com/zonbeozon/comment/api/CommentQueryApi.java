package com.zonbeozon.comment.api;

import com.zonbeozon.comment.dto.CommentsWithAuthorResponse;
import com.zonbeozon.comment.service.CommentAssembler;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import lombok.RequiredArgsConstructor;

@ApiComponent
@RequiredArgsConstructor
public class CommentQueryApi {
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final CommentAssembler commentAssembler;

    public CommentsWithAuthorResponse getComments(Long postId) {
        if(!postAuthorizationCheckService.canAccessChannelContent(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        return commentAssembler.getCommentResponseByPostId(postId);
    }
}
