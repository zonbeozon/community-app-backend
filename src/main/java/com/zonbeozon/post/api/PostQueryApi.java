package com.zonbeozon.post.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PagedRecommendPostResponse;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.service.PostAssembler;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import com.zonbeozon.post.service.recommend.PostRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryApi {
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final PostAssembler postAssembler;
    private final AuthenticationService authenticationService;
    private final PostRecommendService postRecommendService;

    public CursorBasedPostsResponse getCursorBasedPostResponse(
            Long channelId,
            PostCursor cursor,
            int size,
            boolean inverted
    ) {
        if(!channelAuthorizationCheckService.canAccessChannelContent(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Long requesterId = authenticationService.getCurrentMember().getId();
        return postAssembler.getCursorBasedPostResponse(
                requesterId,
                channelId,
                cursor,
                size,
                inverted
        );
    }

    public PostResponse getCursorBasedPostResponse(Long postId) {
        if(!postAuthorizationCheckService.canAccessChannelContent(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Long requesterId = authenticationService.getCurrentMember().getId();
        return postAssembler.getPostResponse(requesterId, postId);
    }

    public PagedRecommendPostResponse getRecommend(Pageable pageable) {
        return postRecommendService.recommend(pageable);
    }

}
