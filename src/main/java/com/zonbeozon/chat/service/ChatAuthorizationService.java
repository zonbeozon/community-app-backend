package com.zonbeozon.chat.service;

import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatAuthorizationService {
    private final ChatFinder chatFinder;

    public void verifyOwner(Long memberId, Long chatId) {
        if(!chatFinder.findByIdElseThrow(chatId).getAuthor().getId().equals(memberId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
    }
}
