package com.zonbeozon.notification.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.notification.dto.PagedNotificationsWithSummaryDto;
import com.zonbeozon.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@RequiredArgsConstructor
public class NotificationApi {
    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    @Transactional(readOnly = true)
    public PagedNotificationsWithSummaryDto getNotifications(Pageable pageable) {
        Member member = authenticationService.getCurrentMember();
        return notificationService.getNotifications(member.getId(), pageable);
    }

    @Transactional
    public void markAsRead() {
        Member member = authenticationService.getCurrentMember();
        notificationService.markAllAsRead(member.getId());
    }
}
