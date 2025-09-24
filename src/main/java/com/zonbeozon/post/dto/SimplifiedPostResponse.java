package com.zonbeozon.post.dto;

import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.dto.ReactionResponse;

import java.time.LocalDateTime;
import java.util.List;

public record SimplifiedPostResponse(
        long postId,
        String content,
        List<ImageDto> images,
        Long authorId,
        Long commentCount,
        Long viewCount,
        ReactionResponse reaction,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SimplifiedPostResponse from(Post post, Long commentCount, ReactionResponse reaction) {
        List<ImageDto> images = post.getImages() == null ? List.of() : post.getImages().stream().map(postImage -> ImageDto.from(postImage.getImage())).toList();
        return new SimplifiedPostResponse(
                post.getId(),
                post.getContent(),
                images,
                post.getAuthor().getId(),
                commentCount,
                post.getViewCount(),
                reaction,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
