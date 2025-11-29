package com.zonbeozon.chat.api;

import com.zonbeozon.chat.domain.ChatCursor;
import com.zonbeozon.chat.dto.PagedChatPayload;
import com.zonbeozon.chat.service.ChatQueryService;
import com.zonbeozon.global.annotation.ApiComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryApi {
    private final ChatQueryService chatQueryService;

    public PagedChatPayload getPagedChatPayload(Long chattingGroupId, ChatCursor cursor, int pageSize) {
        return chatQueryService.getPagedChatPayload(chattingGroupId, cursor, pageSize);
    }
}
