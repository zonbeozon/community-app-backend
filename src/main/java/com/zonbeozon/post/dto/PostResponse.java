package com.zonbeozon.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.reaction.dto.ReactionResponse;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        long postId,
        String content,
        List<ImageDto> images,
        ChannelMemberDto author,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long commentCount,
        Long viewCount,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        ReactionResponse reaction,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post, Long commentCount, ReactionResponse reactionResponse, ChannelMemberDto authorResponse) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getImages().stream().map(PostImage::getImage).map(ImageDto::from).toList(),
                authorResponse,
                commentCount,
                post.getViewCount(),
                reactionResponse,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
