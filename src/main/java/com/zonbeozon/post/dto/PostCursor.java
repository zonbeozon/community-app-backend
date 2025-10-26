package com.zonbeozon.post.dto;

import java.time.LocalDateTime;

public record PostCursor(
        LocalDateTime createdAt,
        Long postId
) {}