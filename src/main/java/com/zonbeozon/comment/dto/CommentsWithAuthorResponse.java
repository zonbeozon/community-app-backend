package com.zonbeozon.comment.dto;

import com.zonbeozon.channel.dto.ChannelMemberDto;

import java.util.List;

public record CommentsWithAuthorResponse(
        List<ChannelMemberDto> authors,
        List<CommentDto> comments,
        int totalElements
) {
}
