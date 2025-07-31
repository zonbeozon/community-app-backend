package com.zonbeozon.post.entity;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@SQLRestriction("is_deleted = false")
@ToString
public class Post extends BaseTimeEntity {
    public static final int MAX_CONTENT_LENGTH = 2048;
    public static final int MIN_CONTENT_LENGTH = 1;
    public static final int MAX_IMAGE_COUNT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private BlogChannel channel;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToMany(mappedBy = "post")
    private List<PostImage> images = new ArrayList<>();

    public static Post create(String content, BlogChannel channel, Member requester) {
        Post post = new Post();
        post.content = content;
        post.author = requester;
        post.channel = channel;
        post.isDeleted = false;
        return post;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deletePost() {
        isDeleted = true;
    }
}
