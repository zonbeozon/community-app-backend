package com.zonbeozon.member.dto;

import com.zonbeozon.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(
        description = "페이징된 멤버 응답",
        example = """
        {
          "content": [
            {
              "id": 1,
              "username": "user1",
              "profile": "https://example.com/profile1.png"
            },
            {
              "id": 2,
              "username": "user2",
              "profile": "https://example.com/profile2.png"
            }
          ],
          "page": 0,
          "size": 10,
          "totalPages": 5,
          "totalElements": 45,
          "isFirst": true,
          "isLast": false,
          "hasNext": true,
          "hasPrevious": false
        }
    """
)
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
