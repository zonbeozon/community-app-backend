package com.zonbeozon.global.viewcount;

import com.zonbeozon.global.entity.ContentEntity;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.service.PostFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public class SimplePostViewCounter implements PostViewCounter {
    private final PostFinder postFinder;

    /**
     * @param viewCounts postId, 증가 시킬 조회수로 이루어진 Map
     */
    public void increase(Map<Long, Long> viewCounts) {
        Map<Long, Post> contents = postFinder.findByIdIn(viewCounts.keySet()).stream()
                .collect(Collectors.toMap(Post::getId, Function.identity()));
        viewCounts.forEach((postId, viewCount) -> {
            contents.get(postId).increaseViewCount(viewCount);
        });
    }

    public void increase(List<Long> postIds) {
        postFinder.findByIdIn(postIds).forEach(post -> post.increaseViewCount(1L));
    }
}
