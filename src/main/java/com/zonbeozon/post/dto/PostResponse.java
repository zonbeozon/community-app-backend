package com.zonbeozon.post.dto;

import com.zonbeozon.member.service.dto.MemberResponse;
import com.zonbeozon.post.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
        long postId,
        String content,
        MemberResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                MemberResponse.from(post.getAuthor()),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
