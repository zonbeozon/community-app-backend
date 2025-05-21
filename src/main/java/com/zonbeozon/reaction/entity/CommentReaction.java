package com.zonbeozon.reaction.entity;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.comment.entity.Comment;
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
            ChannelMember channelMember
    ) {
        super(reactionType, channelMember);
       this.comment = comment;
    }

    public static CommentReaction create(
            Comment comment,
            ReactionType reactionType,
            ChannelMember channelMember
    ) {
        return new CommentReaction(comment, reactionType, channelMember);
    }
}
