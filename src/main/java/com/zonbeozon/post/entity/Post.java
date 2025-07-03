package com.zonbeozon.post.entity;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.PostSupportedChannel;
import com.zonbeozon.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@SQLRestriction("is_deleted = false")
@ToString
public class Post extends BaseTimeEntity {
    public static final int MAX_CONTENT_LENGTH = 2048;
    public static final int MIN_CONTENT_LENGTH = 1;

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
    private PostSupportedChannel channel;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private ChannelMember author;

    public static Post create(String content, PostSupportedChannel channel, ChannelMember author) {
        Post post = new Post();
        post.content = content;
        post.author = author;
        post.channel = channel;
        post.isDeleted = false;
        return post;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isAuthor(ChannelMember channelMember) {
        return author.equals(channelMember);
    }

    public void delete() {
        isDeleted = true;
    }
}
