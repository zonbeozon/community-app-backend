package com.zonbeozon.comment.dto;

public record CommentDeletedEvent(
        Long postId,
        Long commentId
) {
}
