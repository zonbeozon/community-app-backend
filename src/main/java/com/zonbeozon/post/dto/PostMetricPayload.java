package com.zonbeozon.post.dto;

import com.zonbeozon.post.domain.PostMetric;

public record PostMetricPayload(
        Long viewCount,
        Long likeCount,
        Long dislikeCount,
        Long commentCount
) {
    public static PostMetricPayload from(PostMetric postMetric) {
        return new PostMetricPayload(
                postMetric.getViewCount(),
                postMetric.getLikeCount(),
                postMetric.getDislikeCount(),
                postMetric.getCommentCount()
        );
    }
}
