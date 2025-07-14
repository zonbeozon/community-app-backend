package com.zonbeozon.post.repository;

import com.zonbeozon.channel.entity.InfoChannel;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.entity.Post;

public interface PostRepositoryCustom {
    CursorPage<Post> findCursorBasedPostsByChannel(InfoChannel channel, Long cursorPostId, int size);
}
