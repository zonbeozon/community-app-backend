package com.zonbeozon.post.domain;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.reaction.post.entity.PostReaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
@Table(
        indexes = {
                @Index(name = "idx_post_channel_id", columnList = "channel_id"),
                @Index(name = "idx_post_created_at", columnList = "createdAt")
        }
)
public class Post extends BaseTimeEntity {
    public static final int MAX_CONTENT_LENGTH = 2048;
    public static final int MIN_CONTENT_LENGTH = 0;
    public static final int MAX_IMAGE_COUNT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "post")
    @Setter
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostReaction> reactions = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    protected Member author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_metric_id", nullable = false)
    private PostMetric metric;

    protected Post(String content, Channel channel, Member author, PostMetric metric) {
        this.content = content;
        this.channel = channel;
        this.author = author;
        this.metric = metric;
    }

    public static Post create(String content, Channel channel, Member author, PostMetric metric) {
        return new Post(content, channel, author, metric);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Image> getImages() {
        return postImages.stream().map(PostImage::getImage).toList();
    }
}
