package com.zonbeozon.post.repository;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepositoryCustom {
    CursorPage<Post, PostCursor> searchByChannelIdWithMetric(Long channelId, PostCursor cursor, int size, boolean inverted);
    Optional<Post> findByIdWithImages(Long postId);
    Optional<Post> findByIdWithChannelAndImagesAndMetric(Long postId);
    Page<Post> findPostByContentVisibilityOrderByTotalScoreDesc(Pageable pageable, ChannelContentVisibility contentVisibility);
}
