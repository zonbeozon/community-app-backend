package com.zonbeozon.comment.dto;

import com.zonbeozon.comment.entity.Comment;

import java.time.LocalDateTime;

public record SimplifiedCommentResponse(
        String content,
        long authorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SimplifiedCommentResponse from(Comment comment) {
        return new SimplifiedCommentResponse(
                comment.getContent(),
                comment.getAuthor().getId(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
