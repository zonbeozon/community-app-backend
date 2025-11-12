package com.zonbeozon.post.dto;


import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record PagedRecommendPostResponse(
        List<RecommendPostDto> content,
        int totalPages,
        long totalElements,
        int currentPage,
        LocalDateTime lastUpdated
) {
    public static PagedRecommendPostResponse from(Page<RecommendPostDto> page, LocalDateTime lastUpdated) {
        return new PagedRecommendPostResponse(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                lastUpdated
        );
    }
}