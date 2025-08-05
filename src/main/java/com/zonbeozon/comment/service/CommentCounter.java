package com.zonbeozon.comment.service;

import com.zonbeozon.comment.dto.CommentCountResult;
import com.zonbeozon.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentCounter {
    private final CommentRepository commentRepository;

    public Map<Long, Long> countCommentsByPostIdIn(List<Long> postIds) {
        if(postIds == null || postIds.isEmpty()) {
            return new HashMap<>();
        }
        return commentRepository.countCommentsByPostIds(postIds).stream()
                .collect(Collectors.toMap(CommentCountResult::postId, CommentCountResult::count));
    }

    public Long countCommentsByPostId(Long postId) {
        return countCommentsByPostIdIn(List.of(postId)).get(postId);
    }
}
