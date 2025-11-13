package com.zonbeozon.test;

import com.zonbeozon.post.domain.metric.PostMetric;
import com.zonbeozon.post.repository.PostMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestPostMetricService {
    @Autowired
    private PostMetricRepository postMetricRepository;

    @Transactional
    public PostMetric createAndSave(double contentScore, double totalScore) {
        PostMetric postMetric = new PostMetric();
        setContentScore(postMetric, contentScore);
        setTotalScore(postMetric, totalScore);
        return postMetricRepository.save(postMetric);
    }

    private void setContentScore(PostMetric postMetric, double score) {
        ReflectionTestUtils.setField(postMetric, "contentScore", score);
    }

    private void setTotalScore(PostMetric postMetric, double score) {
        ReflectionTestUtils.setField(postMetric, "totalScore", score);
    }
}
