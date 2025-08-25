package com.zonbeozon.member.dto;

import com.zonbeozon.image.entity.ImageResponse;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;

public record MemberResponse(
        Long memberId,
        String username,
        ImageResponse profile,
        ServerRole serverRole
) {
    public static MemberResponse from(Member member) {
        ImageResponse imageResponse = member.getProfile() == null ? null : ImageResponse.from(member.getProfile().getImage());
        return new MemberResponse(
                member.getId(),
                member.getUsername(),
                imageResponse,
                member.getRole()
        );
    }
}
