package com.zonbeozon.post.dto;

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
        Long viewCount,
        ChannelMemberDto author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post, ChannelMemberDto authorResponse) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getPostImages().stream().map(PostImage::getImage).map(ImageDto::from).toList(),
                post.getMetric().getViewCount(),
                authorResponse,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
