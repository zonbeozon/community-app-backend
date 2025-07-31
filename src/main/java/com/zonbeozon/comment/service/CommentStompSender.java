package com.zonbeozon.comment.service;

import com.zonbeozon.comment.dto.CommentCreatedEvent;
import com.zonbeozon.comment.dto.CommentDeletedEvent;
import com.zonbeozon.comment.dto.CommentEventResponse;
import com.zonbeozon.comment.dto.CommentResponse;
import com.zonbeozon.comment.entity.CommentEventType;
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
public class CommentStompSender {
    private final SimpMessagingTemplate messagingTemplate;
    private final CommentAssembler commentAssembler;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentCreated(CommentCreatedEvent event) {
        CommentResponse body = commentAssembler.createCommentResponse(event.commentId());
        messagingTemplate.convertAndSend(
                getDestination(event.postId()),
                new CommentEventResponse(CommentEventType.CREATED, event.commentId(), body)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentDeleted(CommentDeletedEvent event) {
        messagingTemplate.convertAndSend(
                getDestination(event.postId()),
                new CommentEventResponse(CommentEventType.DELETED, event.commentId(), null)
        );
    }

    private String getDestination(Long postId) {
        return "/topic/post/" + postId + "/comment";
    }
}
