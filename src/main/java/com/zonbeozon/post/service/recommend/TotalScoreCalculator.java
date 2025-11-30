package com.zonbeozon.post.service.recommend;

import com.zonbeozon.post.domain.PostMetric;
import com.zonbeozon.post.dto.GlobalScoreStats;
import com.zonbeozon.post.dto.TotalScoreDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class TotalScoreCalculator {
    private static final double WEIGHT_POPULARITY = 0.2;
    private static final double WEIGHT_QUALITY = 0.3;
    private static final double WEIGHT_ENGAGEMENT = 0.3;
    private static final double WEIGHT_CONTENT = 0.2;
    private static final double DECAY_RATE = 0.005;

    private final GlobalScoreStats stats;

    public TotalScoreDto calculate(PostMetric metric) {
        double normContent = normalize(metric.getContentScore(), stats.avgContent(), stats.stdContent());
        double normEngagement = normalize(metric.getEngagementScore(), stats.avgEngagement(), stats.stdEngagement());
        double normQuality = normalize(metric.getQualityScore(), stats.avgQuality(), stats.stdQuality());
        double normPopularity = normalize(metric.getPopularityScore(), stats.avgPopularity(), stats.stdPopularity());

        // 가중치 적용 (합계 1.0)
        double rawTotalScore = (normPopularity * WEIGHT_POPULARITY) +
                (normQuality * WEIGHT_QUALITY) +
                (normEngagement * WEIGHT_ENGAGEMENT) +
                (normContent * WEIGHT_CONTENT);

        // 시간 감쇠 적용
        double finalScore = applyTimeDecay(rawTotalScore, metric.getPost().getCreatedAt());

        return new TotalScoreDto(metric.getId(), finalScore);
    }

    // 정규화 로직: (값) / (평균 + 2*표준편차) -> 상위 5%를 1.0으로 봄
    private double normalize(Double value, double mean, double stdDev) {
        if (value == null) return 0.0;
        double threshold = mean + (2 * stdDev);
        if (threshold <= 0.0001) return 0.0; // 0 나누기 방지
        return Math.min(value / threshold, 1.0); // 1.0 넘으면 자름
    }

    private double applyTimeDecay(double initialScore, LocalDateTime createdAt) {
        long hoursElapsed = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        //생성한 시간에 따라 지수 감쇠
        double decayFactor = Math.exp(-DECAY_RATE * hoursElapsed);
        return initialScore * decayFactor;
    }

}
