package com.zonbeozon.reaction.post.entity;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
@Table(
        name = "post_reaction",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_post_author_type",
                        // 사용자는 포스트 1개에 대해 1개의 리액션만 가능
                        columnNames = {"post_id", "author_id"}
                )
        }
)
public class PostReaction extends Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @NotNull
    private Post post;

    private PostReaction(
            Post post,
            ReactionType  reactionType,
            Member author
    ) {
        super(reactionType, author);
        this.post = post;
    }

    public static PostReaction create(
            Post post,
            ReactionType reactionType,
            Member author
    ) {
        return new PostReaction(post, reactionType, author);
    }
}
