package com.zonbeozon.post.dto;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.entity.Post;

import java.util.List;

public record CursorBasedPostsResponse(
        List<ChannelMemberResponse> authors,
        List<SimplifiedPostResponse> posts,
        int size,
        Long cursorId,
        long totalElements,
        boolean isLast
) {
    public static CursorBasedPostsResponse from(List<ChannelMemberResponse> authors, CursorPage<PostWithStats> posts) {

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
                posts.getCursorId(),
                posts.getTotalElements(),
                posts.isLast()
        );
    }
}
