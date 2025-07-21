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
    public static CursorBasedPostsResponse from(List<ChannelMemberResponse> authors, CursorPage<Post> posts) {

        List<SimplifiedPostResponse> simplifiedPosts = posts.getContent().stream()
                .map(SimplifiedPostResponse::from)  // SimplifiedPostResponse로 변환
                .toList();

        return new CursorBasedPostsResponse(
                authors,
                simplifiedPosts,
                posts.getSize(),             // 페이지 크기 (size)
                posts.getCursorId(),           // 현재 페이지 번호 (page)
                posts.getTotalElements(),     // 총 아이템 수 (totalElements)
                posts.isLast()               // 마지막 페이지 여부 (isLastPage)
        );
    }
}
