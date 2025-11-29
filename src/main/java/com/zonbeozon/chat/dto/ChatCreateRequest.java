package com.zonbeozon.chat.dto;

import com.zonbeozon.chat.domain.Chat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.annotation.Nullable;
import java.util.List;

public record ChatCreateRequest(
        String content,
        @NotNull @Size(min = 0, max = Chat.MAX_IMAGE_COUNT) List<Long> imageIds,
        @Nullable Long parentId
) {
}
