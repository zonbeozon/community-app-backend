package com.zonbeozon.post.controller;

import com.zonbeozon.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record PostAddRequest(
        @Schema(description = "내용", minLength = Post.MIN_CONTENT_LENGTH, maxLength = Post.MAX_CONTENT_LENGTH, example = "some post...")
        @Size(min = Post.MIN_CONTENT_LENGTH, max = Post.MAX_CONTENT_LENGTH, message = "{post.content}")
        String content
) {
}
