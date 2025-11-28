package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.Chat;
import com.zonbeozon.chat.repository.ChatRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatFinder {
    private final ChatRepository chatRepository;

    public Chat findByIdElseThrow(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHAT_NOT_FOUND));
    }

    public Chat findByIdWithChatImagesElseThrow(Long id) {
        return chatRepository.findByIdWithChatImages(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHAT_NOT_FOUND));
    }
}
