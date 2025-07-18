package com.zonbeozon.post.service;


import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.service.BlogChannelFinder;
import com.zonbeozon.post.dto.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LatestPostSetter {
    private final BlogChannelFinder blogChannelFinder;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePostCreated(PostCreatedEvent event) {
        BlogChannel channel = blogChannelFinder.findById(event.channelId());
        channel.setLatestPostId(event.postId());
    }
}
