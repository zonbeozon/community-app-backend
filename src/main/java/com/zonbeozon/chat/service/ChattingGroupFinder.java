package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.ChattingGroup;
import com.zonbeozon.chat.repository.ChattingGroupRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChattingGroupFinder {
    private final ChattingGroupRepository chattingGroupRepository;

    public ChattingGroup findByIdElseThrow(Long id) {
        return chattingGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHATTING_GROUP_NOT_FOUND));
    }

    public Optional<ChattingGroup> findById(Long id) {
        return chattingGroupRepository.findById(id);
    }

    public ChattingGroup findByNameElseThrow(String name) {
        return chattingGroupRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHATTING_GROUP_NOT_FOUND));
    }

    public Optional<ChattingGroup> findByName(String name) {
        return chattingGroupRepository.findByName(name);
    }

    public Map<String, Long> findIdMapByNames(List<String> names) {
        if (names.isEmpty()) return Collections.emptyMap();

        return chattingGroupRepository.findAllByNameIn(names).stream()
                .collect(Collectors.toMap(ChattingGroup::getName, ChattingGroup::getId));
    }
}
