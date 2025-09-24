package com.zonbeozon.notification.dto;

import org.springframework.data.domain.Page;

public record PagedNotificationsWithSummaryDto(
        Page<NotificationDto> pagedNotifications,
        Long totalUnreadCount
) {
}
