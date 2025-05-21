package com.zonbeozon.comment.service.dto;

import com.zonbeozon.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        String content,
        long memberId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse fromEntity(Comment comment) {
        return new CommentResponse(
                comment.getContent(),
                comment.getAuthor().getId(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
