package com.zonbeozon.post.service;

import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostResponse;

import java.util.Collection;
import java.util.Map;

public interface PostAssembler {
    PostResponse getPostResponse(Long postId);
    /**
     * @throws com.zonbeozon.global.exception.NotFoundException postId 중 한개라도 없다면 예외 발생
     * @return postId, PostResponse 로 이루어진 Map
     */
    Map<Long, PostResponse> getPostResponses(Collection<Long> postIds);

    CursorBasedPostsResponse getCursorBasedPostResponse(Long channelId, Long cursorPostId, int size, boolean inverted);

}
