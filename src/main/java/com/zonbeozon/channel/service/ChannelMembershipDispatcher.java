package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.*;
import com.zonbeozon.channel.dto.ChannelMembershipEventPayload;
import com.zonbeozon.channel.enums.ChannelMembershipEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
class ChannelMembershipDispatcher {
    private static final String CHANNEL_MEMBERSHIP_DESTINATION = "/queue/channel-membership";
    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBanned(ChannelMemberBannedEvent event) {
        messagingTemplate.convertAndSendToUser(
                event.memberId().toString(),
                CHANNEL_MEMBERSHIP_DESTINATION,
                new ChannelMembershipEventPayload(ChannelMembershipEventType.BANNED, event.channelId())
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleJoinRequestApproved(JoinRequestApprovedEvent event) {
        messagingTemplate.convertAndSendToUser(
                event.memberId().toString(),
                CHANNEL_MEMBERSHIP_DESTINATION,
                new ChannelMembershipEventPayload(ChannelMembershipEventType.JOIN_REQUEST_APPROVED, event.channelId())
        );
    }
}
