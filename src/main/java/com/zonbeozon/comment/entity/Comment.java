package com.zonbeozon.comment.entity;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.common.entity.BaseTimeEntity;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.entity.CommentReaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Comment extends BaseTimeEntity {
    public static final int MAX_CONTENT_LENGTH = 500;
    public static final int MIN_CONTENT_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_member_id")
    @NotNull
    private ChannelMember author;

    @NotNull
    @ColumnDefault("false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "comment")
    private List<CommentReaction> commentReactions;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void updateContent(final String content) {
        this.content = content;
    }
}
