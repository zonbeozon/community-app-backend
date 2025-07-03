package com.zonbeozon.post.service.dto;

import com.zonbeozon.channel.service.dto.ChannelMemberInfoResponse;
import com.zonbeozon.post.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedPostsResponse(
        List<ChannelMemberInfoResponse> members,
        List<SimplifiedPostResponse> posts,
        int size,
        int page,
        int totalPages,
        long totalElements,
        boolean isLastPage
) {
    public static PagedPostsResponse from(Page<Post> posts) {

        //Member 정보는 중복될 수 있기 떄문에 따로 맵으로 제공
        List<ChannelMemberInfoResponse> members = posts.getContent().stream()
                .map(Post::getAuthor)
                .distinct()
                .map(ChannelMemberInfoResponse::fromEntity)
                .toList();

        List<SimplifiedPostResponse> simplifiedPosts = posts.getContent().stream()
                .map(SimplifiedPostResponse::fromEntity)  // SimplifiedPostResponse로 변환
                .toList();

        return new PagedPostsResponse(
                members,
                simplifiedPosts,
                posts.getSize(),             // 페이지 크기 (size)
                posts.getNumber(),           // 현재 페이지 번호 (page)
                posts.getTotalPages(),       // 총 페이지 수 (totalPages)
                posts.getTotalElements(),    // 총 아이템 수 (totalElements)
                posts.isLast()               // 마지막 페이지 여부 (isLastPage)
        );
    }
}
