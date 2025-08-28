package com.zonbeozon.member.dto;

import com.zonbeozon.member.domain.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedMemberResponse(
    List<MemberResponse> content,
    int page,
    int size,
    int totalPages,
    long totalElements,
    boolean isFirst,
    boolean isLast,
    boolean hasNext,
    boolean hasPrevious
) {
    public static PagedMemberResponse from(Page<Member> pageData) {
        return new PagedMemberResponse(
                pageData.getContent().stream().map(MemberResponse::from).toList(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalPages(),
                pageData.getTotalElements(),
                pageData.isFirst(),
                pageData.isLast(),
                pageData.hasNext(),
                pageData.hasPrevious()
        );
    }
}
