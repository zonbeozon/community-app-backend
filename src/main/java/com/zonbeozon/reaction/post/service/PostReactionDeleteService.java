package com.zonbeozon.reaction.post.service;

import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReactionDeleteService {
    private final PostReactionRepository postReactionRepository;

    public void deleteAll(Long postId) {
        postReactionRepository.deleteByPostId(postId);
    }

    public void deleteAll(List<Long> postIds) {
        postReactionRepository.deleteByPostIdIn(postIds);
    }
}
