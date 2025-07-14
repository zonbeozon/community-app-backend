package com.zonbeozon.post.dto;

import com.zonbeozon.post.entity.Post;

import java.time.LocalDateTime;

public record SimplifiedPostResponse(
        long postId,
        String content,
        Long authorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SimplifiedPostResponse fromEntity(Post post) {
        return new SimplifiedPostResponse(
                post.getId(),
                post.getContent(),
                post.getAuthor().getId(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
