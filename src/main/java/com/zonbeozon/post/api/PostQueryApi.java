package com.zonbeozon.post.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.post.dto.PagedPostsPayload;
import com.zonbeozon.post.dto.PagedRecommendPostPayload;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.dto.PostPayload;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import com.zonbeozon.post.service.PostQueryService;
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
    private final PostQueryService postQueryService;
    private final AuthenticationService authenticationService;
    private final PostRecommendService postRecommendService;

    public PagedPostsPayload getPostPayload(
            Long channelId,
            PostCursor cursor,
            int size,
            boolean inverted
    ) {
        if(!channelAuthorizationCheckService.canAccessChannelContent(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Long requesterId = authenticationService.getCurrentMember().getId();
        return postQueryService.getPagedPostsPayload(requesterId, channelId, cursor, size, inverted);
    }

    public PostPayload getPostPayload(Long postId) {
        if(!postAuthorizationCheckService.canAccessChannelContent(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Long requesterId = authenticationService.getCurrentMember().getId();
        return postQueryService.getPostPayload(requesterId, postId);
    }

    public PagedRecommendPostPayload getRecommend(Pageable pageable) {
        return postRecommendService.recommend(pageable);
    }
}
