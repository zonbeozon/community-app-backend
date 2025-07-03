package com.zonbeozon.post.service;

import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.exception.PostNotFoundException;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.service.dto.*;
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
class PostEventListener {
    private final PostRepository postRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreated(PostCreatedEvent event) {
        PostResponse body = createPostResponse(event.postId());
        messagingTemplate.convertAndSend(
                getDestination(event.channelId()),
                new PostEventResponse(PostEventType.CREATED, event.postId(), body)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostDeleted(PostDeletedEvent event) {
        messagingTemplate.convertAndSend(
                getDestination(event.channelId()),
                new PostEventResponse(PostEventType.DELETED, event.postId(), null)
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdated(PostUpdatedEvent event) {
        PostResponse body = createPostResponse(event.postId());
        messagingTemplate.convertAndSend(
                getDestination(event.channelId()),
                new PostEventResponse(PostEventType.UPDATED, event.postId(), body)
        );
    }

    private String getDestination(Long channelId) {
        return "/topic/channel/" + channelId + "/post";
    }

    private PostResponse createPostResponse(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.fromEntity(post);
    }
}
