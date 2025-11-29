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
public class PostMessageSendService {
    private final SimpMessagingTemplate messagingTemplate;
    private final PostQueryService postQueryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreated(PostEvent.Created event) {
        PostPayload body = postQueryService.getPostPayload(event.postId);
        messagingTemplate.convertAndSend(
                getDestination(event.channelId),
                new PostEventMessage(PostEventType.CREATED, body)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostDeleted(PostEvent.Deleted event) {
        messagingTemplate.convertAndSend(
                getDestination(event.channelId),
                PostEventMessage.createDeleted(event.postId)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdated(PostEvent.Updated event) {
        PostPayload body = postQueryService.getPostPayload(event.postId);
        messagingTemplate.convertAndSend(
                getDestination(event.channelId),
                new PostEventMessage(PostEventType.UPDATED, body)
        );
    }

    private String getDestination(Long channelId) {
        return "/topic/channel/" + channelId + "/post";
    }
}
