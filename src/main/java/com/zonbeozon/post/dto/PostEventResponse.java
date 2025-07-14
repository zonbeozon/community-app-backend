package com.zonbeozon.post.dto;

import org.springframework.lang.Nullable;

public record PostEventResponse(
        PostEventType type,
        Long postId,
        @Nullable PostResponse body
) {
}
