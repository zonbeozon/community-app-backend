package com.zonbeozon.reaction.entity;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.common.entity.BaseTimeEntity;
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
    private ChannelMember author;

    protected Reaction(ReactionType reactionType, ChannelMember author) {
        this.reactionType = reactionType;
        this.author = author;
    }

    public void updateReactionType(final ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
