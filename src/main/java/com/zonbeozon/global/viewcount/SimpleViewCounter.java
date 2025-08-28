package com.zonbeozon.global.viewcount;

import com.zonbeozon.global.entity.ContentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public class SimpleViewCounter implements ViewCounter {
    private final ContentEntityFinder contentEntityFinder;

    /**
     * @param viewCounts postId, 증가 시킬 조회수로 이루어진 Map
     */
    public void increase(Map<Long, Long> viewCounts) {
        Map<Long, ? extends ContentEntity> contents = contentEntityFinder.findByIdIn(viewCounts.keySet()).stream().collect(Collectors.toMap(ContentEntity::getId, Function.identity()));
        viewCounts.forEach((contentId, viewCount) -> {
            contents.get(contentId).increaseViewCount(viewCount);
        });
    }

    public void increase(List<Long> contentIds) {
        contentEntityFinder.findByIdIn(contentIds).forEach(contentEntity -> contentEntity.increaseViewCount(1L));
    }
}
