package com.zonbeozon.post.service;

import com.zonbeozon.channel.service.ChannelLatestEventSetter;
import com.zonbeozon.post.dto.PostCreatedEvent;
import com.zonbeozon.post.dto.PostDeletedEvent;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostActivitySetter {
    private final PostRepository postRepository;
    private final ChannelLatestEventSetter channelLatestEventSetter;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePostCreated(PostCreatedEvent event) {
        channelLatestEventSetter.updateAsNow(event.channelId());

    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePostDeleted(PostDeletedEvent event) {
        Optional<Post> optPost = postRepository.findTopByChannelIdOrderByIdDesc(event.channelId());
        if(optPost.isPresent()) {
            Post post = optPost.get();
            channelLatestEventSetter.update(event.channelId(), post.getCreatedAt());
            return;
        }
        channelLatestEventSetter.updateAsNull(event.channelId());
    }
}
