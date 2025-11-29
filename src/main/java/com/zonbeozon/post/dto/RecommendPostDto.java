package com.zonbeozon.post.dto;

import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.dto.MemberDto;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostImage;

import java.time.LocalDateTime;
import java.util.List;

public record RecommendPostDto(
        long postId,
        long channelId,
        String content,
        List<ImageDto> images,
        PostMetricResponse metric,
        MemberDto author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static RecommendPostDto from(Post post, MemberDto author) {
        return new RecommendPostDto(
                post.getId(),
                post.getChannel().getId(),
                post.getContent(),
                post.getPostImages().stream().map(PostImage::getImage).map(ImageDto::create).toList(),
                PostMetricResponse.from(post.getMetric()),
                author,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
