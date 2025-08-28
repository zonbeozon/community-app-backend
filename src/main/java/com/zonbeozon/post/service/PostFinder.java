package com.zonbeozon.post.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.global.viewcount.ContentEntityFinder;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostFetchOptions;
import com.zonbeozon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostFinder implements ContentEntityFinder {
    private final PostRepository postRepository;

    public Post findByIdElseThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    public Post findByIdElseThrow(Long postId, PostFetchOptions options) {
        return postRepository.findById(postId, options)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    public List<Post> findByIdIn(Collection<Long> postIds) {
        List<Post> posts = postRepository.findAllById(postIds);
        if(posts.size() != postIds.size()) throw new NotFoundException(ErrorCode.POST_NOT_FOUND);
        return posts;
    }

    public List<Post> findByChannelId(Long channelId) {
        return postRepository.findByChannelId(channelId);
    }
}
