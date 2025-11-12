package com.zonbeozon.post.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.service.PostAssembler;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryApi {
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final PostAssembler postAssembler;
    private final AuthenticationService authenticationService;

    public CursorBasedPostsResponse getCursorBasedPostResponse(
            Long channelId,
            PostCursor cursor,
            int size,
            boolean inverted
    ) {
        if(!channelAuthorizationCheckService.canAccessChannelContent(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        return postAssembler.getCursorBasedPostResponse(
                channelId,
                cursor,
                size,
                inverted
        );
    }

    public PostResponse getCursorBasedPostResponse(Long postId) {
        if(!postAuthorizationCheckService.canAccessChannelContent(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        return postAssembler.getPostResponse(postId);
    }
}
