package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelDeletedEvent;
import com.zonbeozon.channel.dto.ChannelEventResponse;
import com.zonbeozon.channel.enums.ChannelEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
class ChannelStatusDispatcher {
    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChannelDeleted(ChannelDeletedEvent event) {
        messagingTemplate.convertAndSend(
                getDestination(event.channelId()),
                new ChannelEventResponse(ChannelEventType.DELETED)
        );
    }

    private String getDestination(Long channelId) {
        return "/topic/channel/" + channelId;
    }
}
