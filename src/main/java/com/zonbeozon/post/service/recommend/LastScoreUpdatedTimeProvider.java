package com.zonbeozon.post.service.recommend;

import java.time.LocalDateTime;

@FunctionalInterface
public interface LastScoreUpdatedTimeProvider {
    LocalDateTime get();
}
