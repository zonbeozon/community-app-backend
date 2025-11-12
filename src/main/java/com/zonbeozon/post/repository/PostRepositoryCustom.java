package com.zonbeozon.post.repository;

import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.domain.Post;

import java.util.Optional;

public interface PostRepositoryCustom {
    CursorPage<Post, PostCursor> searchByChannelIdWithMetric(Long channelId, PostCursor cursor, int size, boolean inverted);
    Optional<Post> findByIdWithImages(Long postId);
    Optional<Post> findByIdWithChannelAndImagesAndMetric(Long postId);
}
