package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.service.BlogChannelFinder;
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
public class LatestPostSetter {
    private final BlogChannelFinder blogChannelFinder;
    private final PostRepository postRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePostCreated(PostCreatedEvent event) {
        BlogChannel channel = blogChannelFinder.findByIdElseThrow(event.channelId());
        channel.setLatestPostId(event.postId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePostDeleted(PostDeletedEvent event) {
        BlogChannel channel = blogChannelFinder.findByIdElseThrow(event.channelId());
        Optional<Post> optPost = postRepository.findTopByChannelOrderByIdDesc(channel);
        if(optPost.isPresent()) {
            Post post = optPost.get();
            channel.setLatestPostId(post.getId());
            return;
        }
        channel.setLatestPostId(null);
    }
}
