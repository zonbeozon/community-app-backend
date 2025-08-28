package com.zonbeozon.post.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PostViewCountRequest(
        @NotEmpty
        List<Long> postIds
) {
}
