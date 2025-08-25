package com.zonbeozon.member.dto;

import com.zonbeozon.member.domain.Member;
import jakarta.validation.constraints.Pattern;

public record UsernameUpdateRequest(
        @Pattern(regexp = Member.ALLOWED_USERNAME_PATTERN, message = "닉네임은 한글, 영문, 숫자, _ 만 가능하며 2~32자여야 합니다.")
        String username
) {
}
