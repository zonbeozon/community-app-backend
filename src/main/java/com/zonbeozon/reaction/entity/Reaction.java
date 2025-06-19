package com.zonbeozon.reaction.entity;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.common.entity.BaseTimeEntity;
import com.zonbeozon.reaction.exception.ReactionAlreadyExistException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
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

    public void validateUpdateReactionType(ReactionType reactionType) {
        //동일 리엑션 타입일때
        if (this.reactionType == reactionType) {
            throw new ReactionAlreadyExistException("이미 리엑션 체크를 했습니다.");
        }
    }

    public void updateReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
