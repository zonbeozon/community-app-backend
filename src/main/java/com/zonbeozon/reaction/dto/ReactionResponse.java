package com.zonbeozon.reaction.dto;

import com.zonbeozon.reaction.enums.ReactionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ReactionResponse {
    private Long likeCount;
    private Long dislikeCount;
    private boolean likedByCurrentMember;
    private boolean dislikeByCurrentMember;

    public ReactionResponse(Map<ReactionType, Long> reactionCounts, boolean likedByCurrentMember, boolean dislikeByCurrentMember) {
        this.likeCount = reactionCounts.get(ReactionType.LIKE);
        this.dislikeCount = reactionCounts.get(ReactionType.DISLIKE);
        this.likedByCurrentMember = likedByCurrentMember;
        this.dislikeByCurrentMember = dislikeByCurrentMember;
    }
}
