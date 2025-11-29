package com.zonbeozon.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

public record PostPayload(
        long postId,
        String content,
        List<ImageDto> images,
        PostMetricPayload metric,
        ChannelMemberDto author,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean isLikedByRequester,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean isDislikedByRequester,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostPayload from(
            Post post,
            @Nullable PersonalizedPostReactionDto personalizedPostReaction,
            ChannelMemberDto authorResponse
    ) {
        return new PostPayload(
                post.getId(),
                post.getContent(),
                post.getImages().stream().map(ImageDto::create).toList(),
                PostMetricPayload.from(post.getMetric()),
                authorResponse,
                personalizedPostReaction == null ? null : personalizedPostReaction.likedByRequester(),
                personalizedPostReaction == null ? null : personalizedPostReaction.dislikedByRequester(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }

    public static PostPayload from(
            PostPayload postPayload,
            PersonalizedPostReactionDto personalizedPostReaction
    ) {
        return new PostPayload(
                postPayload.postId(),
                postPayload.content(),
                postPayload.images(),
                postPayload.metric(),
                postPayload.author(),
                personalizedPostReaction.likedByRequester(),
                personalizedPostReaction.dislikedByRequester(),
                postPayload.createdAt(),
                postPayload.updatedAt()
        );
    }
}
