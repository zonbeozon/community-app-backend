package com.zonbeozon.post.service.metric;

import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostMetric;
import com.zonbeozon.post.dto.PostCreatedEvent;
import com.zonbeozon.post.repository.PostMetricRepository;
import com.zonbeozon.post.service.PostFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class PostMetricCreator {
    private final PostMetricRepository postMetricRepository;
    private final PostFinder postFinder;

    public void create(Long postId) {
        Post post = postFinder.findByIdElseThrow(postId);
        postMetricRepository.save(new PostMetric(post));
    }

    @EventListener
    public void createWhenPostCreated(PostCreatedEvent postCreatedEvent) {
        create(postCreatedEvent.postId());
    }
}
