package com.zonbeozon.reaction.post.entity;

import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class CommentReaction extends Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private CommentReaction(
            Comment comment,
            ReactionType reactionType,
            Member author
    ) {
        super(reactionType, author);
       this.comment = comment;
    }

    public static CommentReaction create(
            Comment comment,
            ReactionType reactionType,
            Member author
    ) {
        return new CommentReaction(comment, reactionType, author);
    }
}
