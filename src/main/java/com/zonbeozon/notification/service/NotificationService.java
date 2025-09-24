package com.zonbeozon.notification.service;

import com.zonbeozon.channel.dto.ChannelMemberBannedEvent;
import com.zonbeozon.channel.dto.JoinRequestApprovedEvent;
import com.zonbeozon.channel.dto.JoinRequestDeniedEvent;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.notification.domain.Notification;
import com.zonbeozon.notification.domain.NotificationType;
import com.zonbeozon.notification.dto.NotificationDto;
import com.zonbeozon.notification.dto.PagedNotificationsWithSummaryDto;
import com.zonbeozon.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberFinder memberFinder;

    @Transactional(readOnly = true)
    public PagedNotificationsWithSummaryDto getNotifications(Long recipientId, Pageable pageable) {
        Page<NotificationDto> notifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId, pageable);
        long unreadCount = notificationRepository.countByRecipientIdAndIsReadFalse(recipientId);
        return new PagedNotificationsWithSummaryDto(notifications, unreadCount);
    }

    @Transactional
    public void markAllAsRead(Long recipientId) {
        notificationRepository.markAllAsReadByRecipientId(recipientId);
    }

    @Transactional
    public NotificationDto createNotification(NotificationType type, String message, Long recipientId) {
        Member member = memberFinder.findByIdElseThrow(recipientId);
        Notification notification = notificationRepository.save(new Notification(type, message, member));
        return new NotificationDto(notification.getType(), notification.getMessage(), notification.isRead(), notification.getCreatedAt());
    }
}
