package com.zonbeozon.post.service;

import com.zonbeozon.post.dto.PostUpdateRequest;
import com.zonbeozon.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostUpdateService {
    private final PostFinder postFinder;
    private final PostImageService postImageService;

    public void updateContent(Long postId, PostUpdateRequest request) {
        Post post = postFinder.findByIdElseThrow(postId);
        post.setContent(request.content());
        postImageService.updatePostImages(postId, request.imageIds());
    }
}
