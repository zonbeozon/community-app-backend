package com.zonbeozon.post.dto;

public record SubScoreDto(
        long postMetricId,
        double contentScore,
        double engagementScore,
        double qualityScore,
        double popularityScore
) {
}
