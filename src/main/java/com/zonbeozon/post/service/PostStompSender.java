package com.zonbeozon.post.service;

import com.zonbeozon.post.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
public class PostStompSender {
    private final SimpMessagingTemplate messagingTemplate;
    private final SimplePostAssembler simplePostAssembler;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreated(PostCreatedEvent event) {
        PostResponse body = simplePostAssembler.getPostResponse(event.postId());
        messagingTemplate.convertAndSend(
                getDestination(event.channelId()),
                new PostEventResponse(PostEventType.CREATED, body)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostDeleted(PostDeletedEvent event) {
        messagingTemplate.convertAndSend(
                getDestination(event.channelId()),
                new PostEventResponse(PostEventType.DELETED, null)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdated(PostUpdatedEvent event) {
        PostResponse body = simplePostAssembler.getPostResponse(event.postId());
        messagingTemplate.convertAndSend(
                getDestination(event.channelId()),
                new PostEventResponse(PostEventType.UPDATED, body)
        );
    }

    private String getDestination(Long channelId) {
        return "/topic/channel/" + channelId + "/post";
    }
}
