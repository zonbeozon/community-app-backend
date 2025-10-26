package com.zonbeozon.reaction.post.dto;

public record PostReactionCountWithPersonalizedDto (
    Long postId,
    Long likeCount,
    Long dislikeCount,
    boolean likedByRequester,
    boolean dislikedByRequester
) {
}
