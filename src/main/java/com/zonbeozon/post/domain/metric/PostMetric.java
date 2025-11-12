package com.zonbeozon.post.domain.metric;

import com.zonbeozon.post.domain.Post;
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
//    private Long commentCount = 0L;
//    private Double contentScore = 0.0;
//    private Double totalScore = 0.0;

    public void setPost(Post post) {
        this.post = post;
    }
}
