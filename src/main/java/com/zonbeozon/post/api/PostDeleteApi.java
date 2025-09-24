package com.zonbeozon.post.api;

import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import com.zonbeozon.post.service.PostRemover;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class PostDeleteApi {
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final PostRemover postRemover;

    public void deletePost(Long postId) {
        if(!postAuthorizationCheckService.isAuthorOrHasHigherRoleThanAuthor(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        postRemover.deletePost(postId);
    }
}
