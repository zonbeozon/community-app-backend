package com.zonbeozon.post.domain;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.global.entity.BaseTimeEntity;
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
                @Index(name = "idx_post_channel_id", columnList = "channel_id")
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
    private BlogChannel channel;

    @OneToMany(mappedBy = "post")
    @Setter
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PostReaction> reactions = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    protected Member author;

    private Long viewCount = 0L;

    protected Post(String content, BlogChannel channel, Member author) {
        this.content = content;
        this.channel = channel;
        this.author = author;
    }

    public static Post create(String content, BlogChannel channel, Member author) {
        return new Post(content, channel, author);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
