package com.zonbeozon.post.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class PostEvent {
    public final Long channelId;
    public final Long postId;

    public static class Created extends PostEvent {
        public Created(Long channelId, Long postId) {
            super(channelId, postId);
        }
    }
    public static class Updated extends PostEvent {
        public Updated(Long channelId, Long postId) {
            super(channelId, postId);
        }
    }
    public static class Deleted extends PostEvent {
        public Deleted(Long channelId, Long postId) {
            super(channelId, postId);
        }
    }
}
