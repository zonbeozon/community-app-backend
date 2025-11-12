package com.zonbeozon.reaction.post.dto;

import com.zonbeozon.reaction.post.entity.ReactionType;

public record PostReactionUpdateEvent(
        Long postId,
        ReactionType updateTo
) {
}
