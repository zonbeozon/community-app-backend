package com.zonbeozon.comment.service.dto;

import com.zonbeozon.comment.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedCommentsResponse(
    List<CommentResponse> comments,
    int size,
    int page,
    int totalPages,
    long totalElements,
    boolean isLastPage
) {
    public static PagedCommentsResponse fromPagedEntity(Page<Comment> comments) {
        List<CommentResponse> commentResponses = comments.getContent().stream()
                .map(CommentResponse::fromEntity)
                .toList();
        return new PagedCommentsResponse(
                commentResponses,
                comments.getSize(),             // 페이지 크기 (size)
                comments.getNumber(),           // 현재 페이지 번호 (page)
                comments.getTotalPages(),       // 총 페이지 수 (totalPages)
                comments.getTotalElements(), // 총 아이템 수 (totalElements)
                comments.isLast()               // 마지막 페이지 여부 (isLastPage)
        );
    }
}


