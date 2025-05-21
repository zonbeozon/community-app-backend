package com.zonbeozon.post.controller;

public record PostUpdateRequest(
        String title,
        String content
) {
}
