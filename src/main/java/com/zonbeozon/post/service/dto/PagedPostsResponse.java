package com.zonbeozon.post.service.dto;

import com.zonbeozon.post.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedPostsResponse(
        List<SimplifiedPostResponse> posts,
        int size,
        int page,
        int totalPages,
        long totalElements,
        boolean isLastPage
) {
    public static PagedPostsResponse from(Page<Post> posts) {
        List<SimplifiedPostResponse> simplifiedPosts = posts.getContent().stream()
                .map(SimplifiedPostResponse::fromEntity)  // SimplifiedPostResponse로 변환
                .toList();

        return new PagedPostsResponse(
                simplifiedPosts,
                posts.getSize(),             // 페이지 크기 (size)
                posts.getNumber(),           // 현재 페이지 번호 (page)
                posts.getTotalPages(),       // 총 페이지 수 (totalPages)
                posts.getTotalElements(),    // 총 아이템 수 (totalElements)
                posts.isLast()               // 마지막 페이지 여부 (isLastPage)
        );
    }
}
