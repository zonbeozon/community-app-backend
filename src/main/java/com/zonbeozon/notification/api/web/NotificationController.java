package com.zonbeozon.notification.api.web;

import com.zonbeozon.notification.api.NotificationApi;
import com.zonbeozon.notification.dto.PagedNotificationsWithSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationApi notificationApi;

    @GetMapping
    public PagedNotificationsWithSummaryDto getNotifications(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationApi.getNotifications(pageable);
    }

    @PostMapping("/mark-as-read")
    public void markAsRead() {
        notificationApi.markAsRead();
    }
}
