package com.zonbeozon.member.dto;

import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;

public record MemberDto(
        Long memberId,
        String username,
        ImageDto profile,
        ServerRole serverRole
) {
    public MemberDto(Long memberId, String username, Long imageId, String imageUrl, ServerRole serverRole) {
        this(
                memberId,
                username,
                imageUrl == null ? null : new ImageDto(imageId, imageUrl),
                serverRole
        );
    }
    public static MemberDto from(Member member, ImageDto profile) {
        return new MemberDto(
                member.getId(),
                member.getUsername(),
                profile,
                member.getRole()
        );
    }
}
