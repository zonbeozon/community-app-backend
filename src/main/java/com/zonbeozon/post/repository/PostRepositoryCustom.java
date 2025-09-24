package com.zonbeozon.post.repository;

import com.zonbeozon.global.LongTypeCursorPage;
import com.zonbeozon.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findById(Long id, PostFetchOptions options);
    LongTypeCursorPage<Post> findCursorBasedPostsByChannelId(Long channelId, Long cursor, int size, boolean inverted);
    Optional<Post> findByIdWithImages(Long postId);
}
