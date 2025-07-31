package com.zonbeozon.comment.dto;

public record CommentCreatedEvent(
        Long postId,
        Long commentId
) {
}
