package com.zonbeozon.chat.service;

import com.zonbeozon.chat.domain.ChatEventType;
import com.zonbeozon.chat.dto.ChatEvent;
import com.zonbeozon.chat.dto.ChatEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
public class ChatMessageSendService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatQueryService chatQueryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatCreated(ChatEvent.Created event) {
        messagingTemplate.convertAndSend(
                getDestination(event.chattingGroupId),
                new ChatEventMessage(
                        ChatEventType.CREATED,
                        chatQueryService.getReplyExecludedChatPayload(event.chatId)
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatDeleted(ChatEvent.Deleted event) {
        messagingTemplate.convertAndSend(
                getDestination(event.chattingGroupId),
                ChatEventMessage.createDeleted(event.chatId)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatUpdated(ChatEvent.Updated event) {
        messagingTemplate.convertAndSend(
                getDestination(event.chattingGroupId),
                new ChatEventMessage(
                        ChatEventType.CREATED,
                        chatQueryService.getReplyExecludedChatPayload(event.chatId)
                )
        );
    }

    private String getDestination(Long channelId) {
        return "/topic/channel/" + channelId + "/post";
    }
}
