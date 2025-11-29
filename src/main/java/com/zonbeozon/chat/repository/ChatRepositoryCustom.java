package com.zonbeozon.chat.repository;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.domain.ChatCursor;
import com.zonbeozon.global.CursorPage;

import java.util.Optional;

public interface ChatRepositoryCustom {
    Optional<Chat> findByIdWithChatImages(Long id);
    Optional<Chat> findByIdWithChatImagesAndAuthor(Long id);
    CursorPage<Chat, ChatCursor> findRootChatByChattingGroupAndCursor(Long chattingGroupId, ChatCursor cursor, int pageSize);
}
