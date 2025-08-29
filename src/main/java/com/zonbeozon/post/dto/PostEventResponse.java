package com.zonbeozon.post.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.lang.Nullable;

public record PostEventResponse(
        PostEventType type,
        @Nullable @JsonUnwrapped
        PostResponse body
) {
}
