package com.zonbeozon.global.viewcount;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 인메모리로 저장해뒀다가 flush-interval-ms 마다 db에 반영한다.
 * 서버가 갑작스럽게 중단될 시 조회수는 flush-interval-ms 정도의 정보 손실이 발생할 수 있다.
 * 사용시 조회수 조회 딜레이가 최대 flush-interval-ms 발생한다.
 */
@Slf4j
public class LazyViewCounter extends SimpleViewCounter implements ViewCounter {
    private final ConcurrentHashMap<Long, Long> viewCountsCache = new ConcurrentHashMap<>();

    public LazyViewCounter(ContentEntityFinder contentEntityFinder) {
        super(contentEntityFinder);
    }

    @Override
    public void increase(Map<Long, Long> viewCounts) {
        if (viewCounts == null) return;
        viewCounts.forEach((contentId, count) ->
                viewCountsCache.merge(contentId, count, Long::sum)
        );
    }

    @Override
    public void increase(List<Long> contentIds) {
        if (contentIds == null) return;
        contentIds.forEach(contentId ->
                viewCountsCache.merge(contentId, 1L, Long::sum)
        );
    }

    @Scheduled(fixedRateString = "${view.count.flush-interval-ms}")
    public void flushViewCountsToDatabase() {
        Map<Long, Long> countsToFlush;
        synchronized (this.viewCountsCache) {
            if (viewCountsCache.isEmpty()) {
                return;
            }
            countsToFlush = new HashMap<>(this.viewCountsCache);
            this.viewCountsCache.clear();
        }
        log.debug("Flushing {} post view counts to database.", countsToFlush.size());

        // 부모 클래스의 DB 업데이트 로직 호출
        super.increase(countsToFlush);
    }
}
