package com.zonbeozon.post.dto;

import com.zonbeozon.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;

public record SimplifiedPostResponse(
        long postId,
        String content,
        List<String> images,
        Long authorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SimplifiedPostResponse from(Post post) {
        List<String> images = post.getImages() == null ? List.of() : post.getImages().stream().map(postImage->postImage.getImage().getUrl()).toList();
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
