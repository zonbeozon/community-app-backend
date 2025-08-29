package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.*;
import com.zonbeozon.channel.enums.ChannelMemberEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
class ChannelMemberStompSender {
    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBanned(ChannelMemberBannedEvent event) {
        messagingTemplate.convertAndSend(
                getDestination(event.channelMemberId().memberId()),
                new ChannelMemberEventResponse(ChannelMemberEventType.BANNED, event.channelMemberId().channelId())
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUnbanned(ChannelMemberUnbannedEvent event) {
        messagingTemplate.convertAndSend(
                getDestination(event.channelMemberId().memberId()),
                new ChannelMemberEventResponse(ChannelMemberEventType.UNBANNED, event.channelMemberId().channelId())
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleJoinRequestApproved(JoinRequestApprovedEvent event) {
        messagingTemplate.convertAndSend(
                getDestination(event.channelMemberId().memberId()),
                new ChannelMemberEventResponse(ChannelMemberEventType.JOIN_REQUEST_APPROVED, event.channelMemberId().channelId())
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleJoinRequestDenied(JoinRequestDeniedEvent event) {
        messagingTemplate.convertAndSend(
                getDestination(event.channelMemberId().memberId()),
                new ChannelMemberEventResponse(ChannelMemberEventType.JOIN_REQUEST_REJECTED, event.channelMemberId().channelId())
        );
    }

    private String getDestination(Long memberId) {
        return "/topic/member/" + memberId + "/channel";
    }
}
