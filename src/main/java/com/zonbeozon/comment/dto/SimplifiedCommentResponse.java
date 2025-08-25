package com.zonbeozon.comment.dto;

import com.zonbeozon.comment.entity.Comment;

import java.time.LocalDateTime;

public record SimplifiedCommentResponse(
        Long commentId,
        Long authorId,
        String content,
        LocalDateTime createdAt
) {
    public static SimplifiedCommentResponse from(Comment comment) {
        return new SimplifiedCommentResponse(
                comment.getId(),
                comment.getAuthor().getId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
