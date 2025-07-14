package com.zonbeozon.post.service;

import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.dto.PostUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostUpdater {
    private final PostFinder postFinder;
    private final ApplicationEventPublisher eventPublisher;

    @CheckChannelAccess(ChannelAction.POST_UPDATE)
    public void updateContent(Long postId, String content) {
        Post post = postFinder.findById(postId);
        post.updateContent(content);
        eventPublisher.publishEvent(new PostUpdatedEvent(post.getChannel().getId(), postId));
    }

}
