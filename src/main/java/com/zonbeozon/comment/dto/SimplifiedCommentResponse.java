package com.zonbeozon.comment.dto;

import com.zonbeozon.comment.entity.Comment;

import java.time.LocalDateTime;

public record SimplifiedCommentResponse(
        Long commentId,
        String content,
        Long authorId,
        LocalDateTime createdAt
) {
    public static SimplifiedCommentResponse from(Comment comment) {
        return new SimplifiedCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getId(),
                comment.getCreatedAt()
        );
    }
}
