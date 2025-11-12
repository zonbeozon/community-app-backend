package com.zonbeozon.post.domain.metric;

import com.zonbeozon.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Long viewCount = 0L;
    private Long likeCount = 0L;
    private Long dislikeCount = 0L;
//    private Long commentCount = 0L;
//    private Double contentScore = 0.0;
//    private Double totalScore = 0.0;

    public PostMetric(Post post) {
        this.post = post;
    }
}
