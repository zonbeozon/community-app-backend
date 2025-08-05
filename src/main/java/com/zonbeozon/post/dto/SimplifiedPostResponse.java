package com.zonbeozon.post.dto;

import com.zonbeozon.image.entity.ImageResponse;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.dto.ReactionResponse;

import java.time.LocalDateTime;
import java.util.List;

public record SimplifiedPostResponse(
        long postId,
        String content,
        List<ImageResponse> images,
        Long authorId,
        Long commentCount,
        ReactionResponse reaction,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SimplifiedPostResponse from(Post post, Long commentCount, ReactionResponse reaction) {
        List<ImageResponse> images = post.getImages() == null ? List.of() : post.getImages().stream().map(postImage -> ImageResponse.from(postImage.getImage())).toList();
        return new SimplifiedPostResponse(
                post.getId(),
                post.getContent(),
                images,
                post.getAuthor().getId(),
                commentCount,
                reaction,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
