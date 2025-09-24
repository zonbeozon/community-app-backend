package com.zonbeozon.reaction.service;

import com.zonbeozon.reaction.enums.ReactionContentType;
import com.zonbeozon.reaction.enums.ReactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ReactionMarker {
    private final List<ReactionMarkHandler> reactionMarkHandlers;
    private final List<ReactionUnmarkHandler> reactionUnmarkHandlers;

    public void mark(Long requesterId, Long contentId, ReactionContentType contentType, ReactionType reactionType) {
        reactionMarkHandlers.stream()
                .filter(handler -> handler.isSupport(contentType))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 타입에 해당하는 헨들러가 등록되지 않았습니다"))
                .mark(requesterId, contentId, reactionType);
    }

    public void unmark(Long requesterId, Long contentId, ReactionContentType contentType) {
        reactionUnmarkHandlers.stream()
                .filter(handler -> handler.isSupport(contentType))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 타입에 해당하는 핸들러가 등록되지 않았습니다"))
                .unmark(requesterId, contentId);
    }
}
