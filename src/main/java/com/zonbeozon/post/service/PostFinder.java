package com.zonbeozon.post.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostFetchOptions;
import com.zonbeozon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostFinder {
    private final PostRepository postRepository;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    public Post findById(Long postId, PostFetchOptions options) {
        return postRepository.findById(postId, options)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    public List<Post> findByIdIn(List<Long> postIds) {
        return postRepository.findAllById(postIds);
    }
}
