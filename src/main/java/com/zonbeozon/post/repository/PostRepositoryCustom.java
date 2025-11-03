package com.zonbeozon.post.repository;

import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.domain.Post;

import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findById(Long id, PostFetchOptions options);
    CursorPage<Post, PostCursor> findCursorBasedPostsByChannelId(Long channelId, PostCursor cursor, int size, boolean inverted);
    Optional<Post> findByIdWithImages(Long postId);
}
