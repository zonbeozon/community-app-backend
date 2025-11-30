package com.zonbeozon.post.dto;

public record GlobalScoreStats(
        double avgContent, double stdContent,
        double avgEngagement, double stdEngagement,
        double avgQuality, double stdQuality,
        double avgPopularity, double stdPopularity
) {
}
