package com.zonbeozon.post.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.dto.PostCreateRequest;
import com.zonbeozon.post.dto.PostEvent;
import com.zonbeozon.post.dto.PostUpdateRequest;
import com.zonbeozon.post.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class PostCommendApi {
    private final PostCreateService postCreateService;
    private final AuthenticationService authenticationService;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final PostDeleteService postDeleteService;
    private final PostUpdateService postUpdateService;
    private final ImageOwnershipVerifier imageOwnershipVerifier;
    private final ApplicationEventPublisher eventPublisher;
    private final PostFinder postFinder;

    public Long createPost(Long channelId, PostCreateRequest request) {
        if(!channelAuthorizationCheckService.isAtLeastAdmin(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        Long postId = postCreateService.createPost(member.getId(), channelId, request.content(), request.imageIds());
        eventPublisher.publishEvent(new PostEvent.Created(channelId, postId));
        return postId;
    }

    public void deletePost(Long postId) {
        if(!postAuthorizationCheckService.isAuthorOrHasHigherRoleThanAuthor(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Post post = postFinder.findByIdElseThrow(postId);
        postDeleteService.delete(postId);
        eventPublisher.publishEvent(new PostEvent.Deleted(post.getChannel().getId(), postId));
    }

    public void updatePost(Long postId, PostUpdateRequest request) {
        Member member = authenticationService.getCurrentMember();
        if(!postAuthorizationCheckService.isAuthor(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        if(!request.imageIds().isEmpty()) imageOwnershipVerifier.verify(member.getId(), request.imageIds());
        postUpdateService.updateContent(postId, request);
        Post post = postFinder.findByIdElseThrow(postId);
        eventPublisher.publishEvent(new PostEvent.Updated(post.getChannel().getId(), postId));
    }
}
