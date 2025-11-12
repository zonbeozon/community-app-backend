package com.zonbeozon.post.dto;

import com.zonbeozon.post.domain.metric.PostMetric;

public record PostMetricResponse(
        Long likeCount,
        Long dislikeCount,
        Long commentCount
) {
    public static PostMetricResponse from(PostMetric postMetric) {
        return new PostMetricResponse(postMetric.getLikeCount(), postMetric.getDislikeCount(), postMetric.getCommentCount());
    }
}
