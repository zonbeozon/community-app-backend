package com.zonbeozon.comment.service;

import com.zonbeozon.comment.dto.*;
import com.zonbeozon.comment.entity.CommentEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CommentStompSender {
    private final SimpMessagingTemplate messagingTemplate;
    private final CommentAssembler commentAssembler;
    private final CommentCounter commentCounter;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentCreated(CommentCreatedEvent event) {
        CommentWithAuthorResponse body = commentAssembler.getCommentResponse(event.commentId());
        messagingTemplate.convertAndSend(
                getCommentDestination(event.postId()),
                new CommentEventResponse(CommentEventType.CREATED, body)
        );
        sendCommentCountUpdate(event.channelId(), event.postId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentDeleted(CommentDeletedEvent event) {
        messagingTemplate.convertAndSend(
                getCommentDestination(event.postId()),
                new CommentEventResponse(CommentEventType.DELETED, null)
        );
        sendCommentCountUpdate(event.channelId(), event.postId());
    }

    private void sendCommentCountUpdate(Long channelId, Long postId) {
        long newCommentCount = commentCounter.countCommentsByPostId(postId);
        messagingTemplate.convertAndSend(
                getCommentCountDestination(channelId),
                new CommentCountEventResponse(postId, newCommentCount)
        );
    }

    private String getCommentDestination(Long postId) {
        return "/topic/post/" + postId + "/comment";
    }

    private String getCommentCountDestination(Long channelId) {
        return "/topic/channel/" + channelId + "/comment-count";
    }
}
