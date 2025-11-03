package com.zonbeozon.post.service.metric.viewcount;

import com.zonbeozon.post.repository.PostMetricRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LazyPostViewCounter extends SimplePostViewCounter implements PostViewCounter, SmartLifecycle {
    private final ConcurrentHashMap<Long, Long> viewCountsCache = new ConcurrentHashMap<>();
    private volatile boolean isRunning = false;

    public LazyPostViewCounter(PostMetricRepository postMetricRepository) {
        super(postMetricRepository);
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

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void stop() {
        flushViewCountsToDatabase();
        this.isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void stop(Runnable callback) {
        log.info("종료전 viewCounts db 반영");
        flushViewCountsToDatabase();
        this.isRunning = false;
        callback.run();
    }
}
