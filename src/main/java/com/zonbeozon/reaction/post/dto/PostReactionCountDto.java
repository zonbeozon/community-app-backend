package com.zonbeozon.reaction.post.dto;

public record PostReactionCountDto(
        Long postId,
        Long likeCount,
        Long dislikeCount
) {
}
