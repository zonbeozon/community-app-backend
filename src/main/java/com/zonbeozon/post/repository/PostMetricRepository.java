package com.zonbeozon.post.repository;

import com.zonbeozon.post.domain.metric.PostMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostMetricRepository extends JpaRepository<PostMetric, Long>, PostMetricRepositoryCustom {
    Optional<PostMetric> findByPostId(Long postId);
}
