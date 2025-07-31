package com.zonbeozon.comment.dto;

import com.zonbeozon.comment.entity.Comment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentAddRequest(
        @Size(min = Comment.MIN_CONTENT_LENGTH, max = Comment.MAX_CONTENT_LENGTH)
        String content
) {
}
