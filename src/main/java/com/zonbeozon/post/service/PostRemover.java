package com.zonbeozon.post.service;

import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.dto.PostDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostRemover {
    private final PostFinder postFinder;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @CheckChannelAccess(ChannelAction.POST_DELETE)
    public void deletePost(Long postId) {
        Post post = postFinder.findById(postId);
        postRepository.delete(post);
        eventPublisher.publishEvent(new PostDeletedEvent(post.getChannel().getId(), postId));
    }
}
