package com.zonbeozon.chat.dto;

import com.zonbeozon.chat.domain.ChatCursor;

import java.util.List;

public record PagedChatPayload(
        Long chattingGroupId,
        List<ChatPayload> content,
        int totalPages,
        long totalElements,
        ChatCursor nextCursor
) {
}
