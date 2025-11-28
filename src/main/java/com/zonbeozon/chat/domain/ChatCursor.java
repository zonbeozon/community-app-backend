package com.zonbeozon.chat.domain;

import java.time.LocalDateTime;

public record ChatCursor(LocalDateTime createdAt, Long chatId) {}
