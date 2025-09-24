package com.zonbeozon.post.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostUpdateRequest;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import com.zonbeozon.post.service.PostUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class PostUpdateApi {
    private final AuthenticationService authenticationService;
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final ImageOwnershipVerifier imageOwnershipVerifier;
    private final PostUpdater postUpdater;

    public void updatePost(Long postId, PostUpdateRequest request) {
        Member member = authenticationService.getCurrentMember();
        if(!postAuthorizationCheckService.isAuthor(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        if(!request.imageIds().isEmpty()) imageOwnershipVerifier.verify(member.getId(), request.imageIds());
        postUpdater.updateContent(postId, request);
    }
}
