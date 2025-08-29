package com.zonbeozon.comment.dto;

public record CommentCreatedEvent(
        Long channelId,
        Long postId,
        Long commentId
) {
}
