package com.zonbeozon.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.dto.MemberDto;

import java.time.LocalDateTime;
import java.util.List;

public record ChatPayload(
        Long chatId,
        String content,
        List<ImageDto> chatImages,
        MemberDto author,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ChatPayload> replies,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
