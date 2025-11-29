package com.zonbeozon.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.image.dto.ImageDto;

import java.time.LocalDateTime;
import java.util.List;

public record PostEventMessage(
        PostEventType type,
        long postId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String content,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ImageDto> images,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        PostMetricPayload metric,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        ChannelMemberDto author,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime createdAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime updatedAt
) {
        public PostEventMessage(PostEventType eventType, PostPayload postPayload) {
                this(
                        eventType,
                        postPayload.postId(),
                        postPayload.content(),
                        postPayload.images(),
                        postPayload.metric(),
                        postPayload.author(),
                        postPayload.createdAt(),
                        postPayload.updatedAt()
                );
        }

        public static PostEventMessage createDeleted(Long postId) {
                return new PostEventMessage(
                        PostEventType.DELETED,
                        postId,
                        null, null, null, null, null, null
                );
        }
}
