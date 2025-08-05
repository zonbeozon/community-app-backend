package com.zonbeozon.comment.dto;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        String content,
        ChannelMemberResponse author,
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment, ChannelMember author) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                ChannelMemberResponse.from(author),
                comment.getCreatedAt()
        );
    }
}