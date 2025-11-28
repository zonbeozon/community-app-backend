package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.repository.ChattingGroupRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChattingGroupFinder {
    private final ChattingGroupRepository chattingGroupRepository;

    public ChattingGroup findByIdElseThrow(Long id) {
        return chattingGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHATTING_GROUP_NOT_FOUND));
    }

    public ChattingGroup findByNameElseThrow(String name) {
        return chattingGroupRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHATTING_GROUP_NOT_FOUND));
    }

    public Optional<ChattingGroup> findByName(String name) {
        return chattingGroupRepository.findByName(name);
    }
}
