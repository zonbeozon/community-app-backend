package com.zonbeozon.chat.dto;

import com.zonbeozon.chat.domain.ChatEventType;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.dto.MemberDto;

import java.time.LocalDateTime;
import java.util.List;

public record ChatEventMessage(
        ChatEventType eventType,
        Long chatId,
        String content,
        List<ImageDto> chatImages,
        MemberDto author,
        LocalDateTime createdAt,
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
}
