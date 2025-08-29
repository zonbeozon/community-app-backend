package com.zonbeozon.comment.dto;

public record CommentCountEventResponse(
        Long postId,
        Long commentCount
) {
}
