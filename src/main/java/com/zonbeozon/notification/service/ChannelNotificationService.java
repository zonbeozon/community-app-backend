package com.zonbeozon.notification.service;

import com.zonbeozon.channel.dto.ChannelMemberBannedEvent;
import com.zonbeozon.channel.dto.JoinRequestApprovedEvent;
import com.zonbeozon.channel.dto.JoinRequestDeniedEvent;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.notification.domain.NotificationType;
import com.zonbeozon.notification.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ChannelNotificationService {
    private final ChannelFinder channelFinder;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private static final String BANNED_FROM_CHANNEL = "'%s' 채널에서 활동이 정지되었습니다.";
    private static final String CHANNEL_JOIN_APPROVED = "'%s' 채널 참여가 승인되었습니다.";
    private static final String CHANNEL_JOIN_REJECTED = "'%s' 채널 참여 요청이 거절되었습니다.";

    private static final String NOTIFICATION_DESTINATION = "/queue/notifications";

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBanned(ChannelMemberBannedEvent event) {
        Channel channel = channelFinder.findByIdElseThrow(event.channelId());
        NotificationDto notification = notificationService.createNotification(
                NotificationType.WARNING,
                String.format(BANNED_FROM_CHANNEL, channel.getTitle()),
                event.memberId()
        );
        sendNotification(event.memberId(), notification);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleJoinRequestApproved(JoinRequestApprovedEvent event) {
        Channel channel = channelFinder.findByIdElseThrow(event.channelId());
        NotificationDto notification = notificationService.createNotification(
                NotificationType.INFO,
                String.format(CHANNEL_JOIN_APPROVED, channel.getTitle()),
                event.memberId()
        );
        sendNotification(event.memberId(), notification);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleJoinRequestDenied(JoinRequestDeniedEvent event) {
        Channel channel = channelFinder.findByIdElseThrow(event.channelId());
        NotificationDto notification = notificationService.createNotification(
                NotificationType.WARNING,
                String.format(CHANNEL_JOIN_REJECTED, channel.getTitle()),
                event.memberId()
        );
        sendNotification(event.memberId(), notification);
    }

    private void sendNotification(Long userId, NotificationDto notification) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                NOTIFICATION_DESTINATION,
                notification
        );
    }
}
