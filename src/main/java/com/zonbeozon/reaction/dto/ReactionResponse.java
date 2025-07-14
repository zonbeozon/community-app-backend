package com.zonbeozon.reaction.dto;

import com.zonbeozon.reaction.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ReactionResponse {
    private Long likeCount;
    private Long dislikeCount;
    private boolean likedByCurrentMember;
    private boolean dislikeByCurrentMember;

    public ReactionResponse(Map<ReactionType, Long> reactionTypeCount) {
        this.likeCount = reactionTypeCount.getOrDefault(ReactionType.LIKE, 0L);
        this.dislikeCount = reactionTypeCount.getOrDefault(ReactionType.DISLIKE, 0L);
    }

    public void setReactionByCurrentMember(ReactionType reactionType) {
        if(reactionType.equals(ReactionType.LIKE)) {
            likedByCurrentMember = true;
        }
        if(reactionType.equals(ReactionType.DISLIKE)) {
            dislikeByCurrentMember = true;
        }
    }
}
