package com.zonbeozon.comment.dto;

import com.zonbeozon.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long commentId,
        String content,
        Long authorId,
        LocalDateTime createdAt
) {
    public static CommentDto from(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getId(),
                comment.getCreatedAt()
        );
    }
}