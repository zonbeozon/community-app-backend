package com.zonbeozon.post.repository;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.entity.Post;

public interface PostRepositoryCustom {
    CursorPage<Post> findCursorBasedPostsByChannel(BlogChannel channel, Long cursorPostId, int size);
    void softDeleteAllByChannelId(Long channelId);
}
