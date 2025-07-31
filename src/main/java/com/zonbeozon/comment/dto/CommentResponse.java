package com.zonbeozon.comment.dto;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        String content,
        ChannelMemberResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment, ChannelMember author) {
        return new CommentResponse(
                comment.getContent(),
                ChannelMemberResponse.from(author),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}