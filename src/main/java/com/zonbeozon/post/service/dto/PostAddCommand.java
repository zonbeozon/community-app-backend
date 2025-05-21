package com.zonbeozon.post.service.dto;

public record PostAddCommand(
        String title,
        String content
) {
}
