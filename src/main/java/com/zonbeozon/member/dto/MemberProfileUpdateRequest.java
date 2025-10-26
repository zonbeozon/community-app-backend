package com.zonbeozon.member.dto;

import jakarta.validation.constraints.NotNull;

public record MemberProfileUpdateRequest(
        @NotNull Long imageId
) {
}
