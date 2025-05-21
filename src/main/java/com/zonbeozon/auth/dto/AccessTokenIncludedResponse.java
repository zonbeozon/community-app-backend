package com.zonbeozon.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AccessTokenIncludedResponse(@NotBlank String accessToken) {
}
