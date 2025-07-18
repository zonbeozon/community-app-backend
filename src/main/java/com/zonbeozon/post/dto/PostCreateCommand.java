package com.zonbeozon.post.dto;

import java.util.List;

public record PostCreateCommand(
        String content,
        List<Long> imageIds
) {
}
