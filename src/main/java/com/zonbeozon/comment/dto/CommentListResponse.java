package com.zonbeozon.comment.dto;

import com.zonbeozon.channel.dto.ChannelMemberResponse;

import java.util.List;

public record CommentListResponse (
        List<ChannelMemberResponse> authors,
        List<SimplifiedCommentResponse> comments,
        int totalElements
) {
}
