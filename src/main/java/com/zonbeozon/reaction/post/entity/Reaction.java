package com.zonbeozon.reaction.post.entity;

import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Reaction extends BaseTimeEntity {
    @NotNull
    private ReactionType reactionType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    protected Reaction(ReactionType reactionType, Member author) {
        this.reactionType = reactionType;
        this.author = author;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
