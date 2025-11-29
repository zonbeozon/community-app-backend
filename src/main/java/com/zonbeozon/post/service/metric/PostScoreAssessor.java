package com.zonbeozon.post.service.metric;

import com.zonbeozon.post.domain.PostMetric;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class PostScoreAssessor {
    private static final double WEIGHT_CONTENT_SCORE = 2.0;
    private static final double WEIGHT_COMMENT_COUNT = 1.5;
    private static final double WEIGHT_LIKE_COUNT = 0.5;
    private static final double WEIGHT_DISLIKE_COUNT = -1.0;
    private static final double WEIGHT_VIEW_COUNT = 0.1;
    private static final double DECAY_RATE = 0.005;

    public double assess(PostMetric metric, double contentScore) {
        double postScore = (contentScore * WEIGHT_CONTENT_SCORE) +
                (metric.getCommentCount() * WEIGHT_COMMENT_COUNT) +
                (metric.getLikeCount() * WEIGHT_LIKE_COUNT) +
                (metric.getDislikeCount() * WEIGHT_DISLIKE_COUNT) +
                (metric.getViewCount() * WEIGHT_VIEW_COUNT);

        return Math.max(0.0, applyTimeDecay(postScore, metric.getPost().getCreatedAt()));
    }

    private double applyTimeDecay(double initialScore, LocalDateTime createdAt) {
        long hoursElapsed = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        //생성한 시간에 따라 지수 감쇠 발생
        double decayFactor = Math.exp(-DECAY_RATE * hoursElapsed);
        return initialScore * decayFactor;
    }
}
