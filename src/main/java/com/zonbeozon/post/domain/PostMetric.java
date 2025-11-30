package com.zonbeozon.post.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "metric", optional = false)
    private Post post;

    private Long viewCount = 0L;
    private Long likeCount = 0L;
    private Long dislikeCount = 0L;
    private Long commentCount = 0L;

    private Double contentScore;
    private Double engagementScore;
    private Double qualityScore;
    private Double popularityScore;

    private Double totalScore;

    public void setPost(Post post) {
        this.post = post;
    }
}
