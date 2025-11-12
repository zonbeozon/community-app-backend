package com.zonbeozon.post.service;

import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.dto.PostResponse;

public interface PostAssembler {
    PostResponse getPostResponse(Long requesterId, Long postId);
    CursorBasedPostsResponse getCursorBasedPostResponse(
            Long requesterId,
            Long channelId,
            PostCursor cursor,
            int size,
            boolean inverted
    );
}
