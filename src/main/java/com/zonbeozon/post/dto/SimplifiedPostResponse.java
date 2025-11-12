package com.zonbeozon.post.dto;

import com.zonbeozon.image.dto.ImageDto;

import java.time.LocalDateTime;
import java.util.List;

public record SimplifiedPostResponse(
        long postId,
        String content,
        List<ImageDto> images,
        PostMetricResponse metric,
        boolean isLikedByRequester,
        boolean isDislikedByRequester,
        Long authorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
