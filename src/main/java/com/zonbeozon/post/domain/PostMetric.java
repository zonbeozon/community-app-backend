package com.zonbeozon.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMetric {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    private Long viewCount = 0L;
//    private Long likeCount = 0L;
//    private Long viewCount = 0L;
//    private Long commentCount = 0L;
//    private Double contentScore = 0.0;
//    private Double totalScore = 0.0;

    public PostMetric(Post post) {
        this.post = post;
    }
}
