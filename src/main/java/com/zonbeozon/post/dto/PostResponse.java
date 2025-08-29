package com.zonbeozon.post.dto;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.image.entity.ImageResponse;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.reaction.dto.ReactionResponse;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        long postId,
        String content,
        List<ImageResponse> images,
        ChannelMemberResponse author,
        Long commentCount,
        Long viewCount,
        ReactionResponse reaction,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post, Long commentCount, ReactionResponse reactionResponse, ChannelMemberResponse authorResponse) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getImages().stream().map(PostImage::getImage).map(ImageResponse::from).toList(),
                authorResponse,
                commentCount,
                post.getViewCount(),
                reactionResponse,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
