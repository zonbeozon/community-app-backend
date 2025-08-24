package com.zonbeozon.post.repository;

import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.entity.Post;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findById(Long id, PostFetchOptions options);
    CursorPage<Post> findCursorBasedPostsByChannelId(Long channelId, Long cursorPostId, int size);
    List<Post> findByIdInWithImagesAndAuthorAndChannel(Collection<Long> postIds);
}
