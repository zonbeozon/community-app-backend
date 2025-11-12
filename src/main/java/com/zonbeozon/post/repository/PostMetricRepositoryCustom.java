package com.zonbeozon.post.repository;

import com.zonbeozon.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PostMetricRepositoryCustom {
    /**
     *
     * @param viewCounts postId-증가시킬 조회수 로 이루어진 Map
     */
    long updateViewCounts(Map<Long, Long> viewCounts);
    void updateLikeCount(Long postId, Long delta);
    void updateDislikeCount(Long postId, Long delta);
    void updateCommentCount(Long postId, Long delta);
}
