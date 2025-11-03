package com.zonbeozon.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostImage;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        long postId,
        String content,
        List<ImageDto> images,
        ChannelMemberDto author,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post, Long commentCount, ChannelMemberDto authorResponse) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getPostImages().stream().map(PostImage::getImage).map(ImageDto::from).toList(),
                authorResponse,
                commentCount,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
