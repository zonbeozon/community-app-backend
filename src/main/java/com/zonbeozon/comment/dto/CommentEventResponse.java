package com.zonbeozon.comment.dto;

import com.zonbeozon.comment.entity.CommentEventType;
import org.springframework.lang.Nullable;

public record CommentEventResponse (
    CommentEventType type,
    Long commentId,
    @Nullable
    CommentResponse body
) {
}
