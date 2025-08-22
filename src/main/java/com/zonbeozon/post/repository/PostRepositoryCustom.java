package com.zonbeozon.post.repository;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findById(Long id, PostFetchOptions options);
    CursorPage<Post> findCursorBasedPostsByChannel(BlogChannel channel, Long cursorPostId, int size);
}
