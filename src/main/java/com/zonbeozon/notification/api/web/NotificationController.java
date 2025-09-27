package com.zonbeozon.notification.api.web;

import com.zonbeozon.notification.api.NotificationApi;
import com.zonbeozon.notification.dto.PagedNotificationsWithSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationApi notificationApi;

    @GetMapping
    public PagedNotificationsWithSummaryDto getNotifications(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationApi.getNotifications(pageable);
    }

    @PostMapping("/mark-as-read")
    public void markAsRead() {
        notificationApi.markAsRead();
    }
}
