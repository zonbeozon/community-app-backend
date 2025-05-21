package com.zonbeozon.member.dto;

import com.zonbeozon.member.domain.ServerRole;

public record MemberResponse(
        Long id,
        String username,
        String profile,
        ServerRole serverRole
) {
}
