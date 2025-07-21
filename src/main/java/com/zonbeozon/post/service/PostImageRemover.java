package com.zonbeozon.post.service;

import com.zonbeozon.post.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageRemover {
    private final PostImageRepository postImageRepository;

    public void deletePostImages(Long postId) {
        postImageRepository.deleteByPostId(postId);
    }
}
