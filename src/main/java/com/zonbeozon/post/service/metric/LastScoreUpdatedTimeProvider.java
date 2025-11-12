package com.zonbeozon.post.service.metric;

import java.time.LocalDateTime;

@FunctionalInterface
public interface LastScoreUpdatedTimeProvider {
    LocalDateTime get();
}
