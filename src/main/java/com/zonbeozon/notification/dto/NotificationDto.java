package com.zonbeozon.notification.dto;

import com.zonbeozon.notification.domain.NotificationType;

import java.time.LocalDateTime;

public record NotificationDto(
        NotificationType type,
        String message,
        boolean isRead,
        LocalDateTime createdAt
){}
