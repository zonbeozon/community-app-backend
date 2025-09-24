package com.zonbeozon.comment.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.zonbeozon.comment.entity.CommentEventType;
import org.springframework.lang.Nullable;

public record CommentEventResponse (
    CommentEventType type,
    @Nullable
    @JsonUnwrapped
    CommentWithAuthorResponse body
) {
}
