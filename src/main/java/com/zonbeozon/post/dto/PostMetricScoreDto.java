package com.zonbeozon.post.dto;

public record PostMetricScoreDto(
        long postMetricId,
        double contentScore,
        double totalScore
) {
}
