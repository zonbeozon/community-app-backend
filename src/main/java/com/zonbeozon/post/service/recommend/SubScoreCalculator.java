package com.zonbeozon.post.service.recommend;

import com.zonbeozon.post.domain.PostMetric;
import org.springframework.stereotype.Component;

@Component
public class SubScoreCalculator {
    private static final double COMMENT_MULTIPLIER = 3.0;
    private static final double ENGAGEMENT_BUFFER = 50.0;

    /**
     * 댓글에는 가중치를 적용하여 계산
     * ENGAGEMENT_BUFFER(Bayesian Smoothing)을 적용하여 소규모 채널에서의 값이 튀는걸 방지
     */
    public double calculateEngagementScore(PostMetric metric) {
        double totalInteractions = metric.getLikeCount() + metric.getDislikeCount() + (metric.getCommentCount() * COMMENT_MULTIPLIER);
        return totalInteractions / (metric.getViewCount() + ENGAGEMENT_BUFFER);
    }

    public double calculateQualityScore(PostMetric metric) {
        return calculateWilsonScore(metric.getLikeCount(), metric.getLikeCount() + metric.getDislikeCount());
    }

    public double calculatePopularityScore(PostMetric metric) {
        return Math.log10(metric.getViewCount() + 1);
    }

    /**
     * Wilson Score Interval (Lower bound)
     * 좋아요 비율의 하한선을 구해서, 표본이 적을 때의 불확실성을 보정함
     *
     * @param positive 좋아요 수
     * @param total    전체 투표 수 (좋아요 + 싫어요)
     * @return 0.0 ~ 1.0 사이의 점수
     */
    private double calculateWilsonScore(long positive, long total) {
        if (total == 0) return 0.0;

        double z = 1.96; // 95% 신뢰구간
        double phat = (double) positive / total; // 관측된 성공 비율

        double numerator = phat + (z * z) / (2 * total) - z * Math.sqrt((phat * (1 - phat) + (z * z) / (4 * total)) / total);
        double denominator = 1 + (z * z) / total;

        return numerator / denominator;
    }
}
