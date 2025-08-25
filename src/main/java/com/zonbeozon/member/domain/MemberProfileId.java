package com.zonbeozon.member.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record MemberProfileId(
        Long memberId,
        Long imageId
) {
}
