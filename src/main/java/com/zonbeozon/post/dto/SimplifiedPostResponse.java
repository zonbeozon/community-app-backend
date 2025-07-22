package com.zonbeozon.post.dto;

import com.zonbeozon.image.entity.ImageResponse;
import com.zonbeozon.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;

public record SimplifiedPostResponse(
        long postId,
        String content,
        List<ImageResponse> images,
        Long authorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SimplifiedPostResponse from(Post post) {
        List<ImageResponse> images = post.getImages() == null ? List.of() : post.getImages().stream().map(postImage -> ImageResponse.from(postImage.getImage())).toList();
        return new SimplifiedPostResponse(
                post.getId(),
                post.getContent(),
                images,
                post.getAuthor().getId(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
