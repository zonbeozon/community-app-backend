package com.zonbeozon.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zonbeozon.chat.domain.ChatEventType;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.dto.MemberDto;

import java.time.LocalDateTime;
import java.util.List;

public record ChatEventMessage(
        ChatEventType eventType,
        Long chatId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String content,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ImageDto> chatImages,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        MemberDto author,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime createdAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime updatedAt
) {
    public ChatEventMessage(ChatEventType eventType, ChatPayload chatPayload) {
        this(
                eventType,
                chatPayload.chatId(),
                chatPayload.content(),
                chatPayload.chatImages(),
                chatPayload.author(),
                chatPayload.createdAt(),
                chatPayload.updatedAt()
        );
    }

    public static ChatEventMessage createDeleted(Long chatId) {
        return new ChatEventMessage(
             ChatEventType.DELETED,
             chatId,
             null, null, null, null, null
        );
    }
}
