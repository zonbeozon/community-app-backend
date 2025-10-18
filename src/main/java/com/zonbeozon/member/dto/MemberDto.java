package com.zonbeozon.member.dto;

import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import org.springframework.lang.Nullable;

public record MemberDto(
        Long memberId,
        String username,
        ImageDto profile,
        ServerRole serverRole
) {
    public static MemberDto from(Member member, ImageDto profile) {
        return new MemberDto(
                member.getId(),
                member.getUsername(),
                profile,
                member.getRole()
        );
    }
}
