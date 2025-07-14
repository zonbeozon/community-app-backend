package com.zonbeozon.reaction.entity;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.enums.ReactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
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
            ReactionType reactionType,
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
