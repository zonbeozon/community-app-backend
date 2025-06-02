package com.zonbeozon.post.service.dto;

import com.zonbeozon.channel.service.dto.ChannelMemberInfoResponse;
import com.zonbeozon.post.entity.Post;

import java.time.LocalDateTime;

public record SimplifiedPostResponse(
        long postId,
        String content,
        ChannelMemberInfoResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SimplifiedPostResponse fromEntity(Post post) {
        return new SimplifiedPostResponse(
                post.getId(),
                post.getContent(),
                ChannelMemberInfoResponse.fromEntity(post.getAuthor()),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
