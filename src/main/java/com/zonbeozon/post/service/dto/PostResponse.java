package com.zonbeozon.post.service.dto;

import com.zonbeozon.post.entity.Post;
import com.zonbeozon.channel.service.dto.ChannelMemberInfoResponse;

import java.time.LocalDateTime;

public record PostResponse(
        String content,
        ChannelMemberInfoResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(
            Post post
    ) {
        return new PostResponse(
                post.getContent(),
                ChannelMemberInfoResponse.fromEntity(post.getAuthor()),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
