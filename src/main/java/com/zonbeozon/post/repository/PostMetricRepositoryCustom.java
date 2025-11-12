package com.zonbeozon.post.repository;

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
