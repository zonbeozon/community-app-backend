package com.zonbeozon.post.service;

import com.zonbeozon.post.dto.PostUpdateRequest;
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
    private final PostImageService postImageService;
    private final ApplicationEventPublisher eventPublisher;

    public void updateContent(Long postId, PostUpdateRequest request) {
        Post post = postFinder.findByIdElseThrow(postId);
        post.updateContent(request.content());
        postImageService.updatePostImages(postId, request.imageIds());
        eventPublisher.publishEvent(new PostUpdatedEvent(post.getChannel().getId(), postId));
    }
}
