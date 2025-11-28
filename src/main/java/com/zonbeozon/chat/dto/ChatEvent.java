package com.zonbeozon.chat.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatEvent {
    public final Long chattingGroupId;
    public final Long chatId;

    public static class Created extends ChatEvent {
        public Created(Long chattingGroupId, Long chatId) {
            super(chattingGroupId, chatId);
        }
    }
    public static class Deleted extends ChatEvent {
        public Deleted(Long chattingGroupId, Long chatId) {
            super(chattingGroupId, chatId);
        }
    }
    public static class Updated extends ChatEvent {
        public Updated(Long chattingGroupId, Long chatId) {
            super(chattingGroupId, chatId);
        }
    }
}
