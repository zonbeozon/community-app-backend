package com.zonbeozon.comment.service.dto;

public record CommentAddRequest(
    String content,
    Long postId
) {
}
