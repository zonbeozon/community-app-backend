package com.zonbeozon.post.entity;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.global.entity.ContentEntity;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.reaction.entity.PostReaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class Post extends ContentEntity {
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
    private Set<PostImage> images = new HashSet<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PostReaction> reactions = new ArrayList<>();

    protected Post(String content, BlogChannel channel, Member author) {
        super(author);
        this.content = content;
        this.channel = channel;
    }

    public static Post create(String content, BlogChannel channel, Member author) {
        return new Post(content, channel, author);
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
