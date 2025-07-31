package com.zonbeozon.post.service;

import com.zonbeozon.channel.dto.ChannelDeletedEvent;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.dto.PostDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class PostRemover {
    private final PostFinder postFinder;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @CheckChannelAccess(ChannelAction.POST_DELETE)
    @Transactional
    public void deletePost(Long postId) {
        Post post = postFinder.findById(postId);
        post.deletePost();
        eventPublisher.publishEvent(new PostDeletedEvent(post.getChannel().getId(), postId));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleChannelDeleted(ChannelDeletedEvent event) {
        postRepository.softDeleteAllByChannelId(event.channelId());
    }
}
