package com.zonbeozon.post.dto;

import com.zonbeozon.post.domain.metric.PostMetric;

public record PostMetricResponse(
        Long viewCount,
        Long likeCount,
        Long dislikeCount,
        Long commentCount
) {
    public static PostMetricResponse from(PostMetric postMetric) {
        return new PostMetricResponse(
                postMetric.getViewCount(),
                postMetric.getLikeCount(),
                postMetric.getDislikeCount(),
                postMetric.getCommentCount()
        );
    }
}
