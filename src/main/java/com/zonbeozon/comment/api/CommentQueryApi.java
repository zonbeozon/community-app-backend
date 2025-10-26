package com.zonbeozon.comment.api;

import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.comment.dto.CommentsWithAuthorResponse;
import com.zonbeozon.comment.service.CommentAssembler;
import com.zonbeozon.comment.service.CommentCounter;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@ApiComponent
@RequiredArgsConstructor
public class CommentQueryApi {
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final CommentAssembler commentAssembler;
    private final CommentCounter commentCounter;

    public CommentsWithAuthorResponse getComments(Long postId) {
        if(!postAuthorizationCheckService.canAccessChannelContent(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        return commentAssembler.getCommentResponseByPostId(postId);
    }

    public Map<Long, Long> getCommentCountsByPostIds(Long channelId, List<Long> postIds) {
        if(!channelAuthorizationCheckService.canAccessChannelContent(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        postAuthorizationCheckService.validatePostsInChannel(channelId, postIds);
        return commentCounter.countCommentsByPostIdIn(postIds);
    }
}
