package com.zonbeozon.reaction.post.dto;

public record PersonalizedPostReactionDto(
        Long postId,
        boolean likedByRequester,
        boolean dislikedByRequester
) {
}
