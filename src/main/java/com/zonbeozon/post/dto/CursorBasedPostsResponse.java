package com.zonbeozon.post.dto;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.global.LongTypeCursorPage;

import java.util.List;

public record CursorBasedPostsResponse(
        List<ChannelMemberDto> authors,
        List<SimplifiedPostResponse> posts,
        int size,
        Long cursor,
        long totalElements,
        boolean isLast,
        boolean isInverted
) {
    public static CursorBasedPostsResponse from(
            List<ChannelMemberDto> authors,
            LongTypeCursorPage<PostWithStats> posts
    ) {

        List<SimplifiedPostResponse> simplifiedPosts = posts.getContent().stream()
                .map(postWithStats -> SimplifiedPostResponse.from(
                        postWithStats.getPost(),
                        postWithStats.getCommentCount(),
                        postWithStats.getReactionResponse()))
                .toList();

        return new CursorBasedPostsResponse(
                authors,
                simplifiedPosts,
                posts.getSize(),
                posts.getCursor(),
                posts.getTotalElements(),
                posts.isLast(),
                posts.isInverted()
        );
    }
}
