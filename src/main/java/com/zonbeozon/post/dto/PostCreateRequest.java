package com.zonbeozon.post.dto;

import com.zonbeozon.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostCreateRequest(
        @Schema(minLength = Post.MIN_CONTENT_LENGTH, maxLength = Post.MAX_CONTENT_LENGTH, example = "some post...")
        @Size(min = Post.MIN_CONTENT_LENGTH, max = Post.MAX_CONTENT_LENGTH, message = "{post.content}")
        String content,
        @Schema(description = "이미지 업로드로 부터 받은 응답Id를 리스트 형식으로 전달")
        @NotNull(message = "이미지id 필드를 포함해야 됩니다.")
        List<Long> imageIds
) {
}
