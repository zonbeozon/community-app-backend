package com.zonbeozon.post.repository;

import java.util.Map;

public interface PostMetricRepositoryCustom {
    long updateViewCounts(Map<Long, Long> viewCounts);
}
