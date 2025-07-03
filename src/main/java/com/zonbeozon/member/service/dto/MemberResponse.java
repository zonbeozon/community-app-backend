package com.zonbeozon.member.service.dto;

import com.zonbeozon.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "id가 1인 맴버를 조회했을 때 ",
        example = """
        {
          "id": 1,
          "username": "user1",
          "profile": "https://example.com/profile1.png"
        }
    """
)
public record MemberResponse(
        Long id,
        String username,
        String profile
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getUsername(), member.getProfile());
    }
}
