package com.zonbeozon.post.dto;


import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record PagedRecommendPostPayload(
        List<RecommendPostDto> content,
        int totalPages,
        long totalElements,
        int currentPage,
        LocalDateTime lastUpdated
) {
    public static PagedRecommendPostPayload from(Page<RecommendPostDto> page, LocalDateTime lastUpdated) {
        return new PagedRecommendPostPayload(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                lastUpdated
        );
    }
}