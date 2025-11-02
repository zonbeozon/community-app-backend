package com.zonbeozon.post.service.viewcount;

import com.zonbeozon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public class SimplePostViewCounter implements PostViewCounter {
    private final PostRepository postRepository;
    /**
     * @param viewCounts postId, 증가 시킬 조회수로 이루어진 Map
     */
    public void increase(Map<Long, Long> viewCounts) {
        postRepository.updateViewCounts(viewCounts);
    }

    public void increase(List<Long> postIds) {
        Map<Long, Long> viewCounts = postIds.stream().collect(Collectors.groupingBy(id -> id, Collectors.counting()));
        increase(viewCounts);
    }
}
