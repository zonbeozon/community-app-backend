package com.zonbeozon.comment.dto;

import com.zonbeozon.channel.dto.ChannelMemberDto;

import java.time.LocalDateTime;

public record CommentWithAuthorResponse(
    Long commentId,
    String content,
    ChannelMemberDto author,
    LocalDateTime createdAt
) {}
