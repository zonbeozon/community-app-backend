package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;

import java.time.LocalDateTime;

public record LatestPostResponse(
        Long postId,
        String content,
        Long imageCount,
        ChannelMemberResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static LatestPostResponse from(ChannelRole authorRole, Member author, Post post, Long imageCount) {
        return new LatestPostResponse(
                post.getId(),
                post.getContent(),
                imageCount,
                ChannelMemberResponse.from(author, authorRole),
                post.getCreatedAt(),
                post.getModifiedAt());
    }
}
