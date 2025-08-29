package com.zonbeozon.comment.dto;

public record CommentDeletedEvent(
        Long channelId,
        Long postId,
        Long commentId
) {
}
