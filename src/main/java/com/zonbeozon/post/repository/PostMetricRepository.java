package com.zonbeozon.post.repository;

import com.zonbeozon.post.domain.PostMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMetricRepository extends JpaRepository<Long, PostMetric>, PostMetricRepositoryCustom {
}
