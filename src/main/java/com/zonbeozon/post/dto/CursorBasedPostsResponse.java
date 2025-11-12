package com.zonbeozon.post.dto;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;

import java.util.List;
import java.util.Map;

public record CursorBasedPostsResponse(
        List<ChannelMemberDto> authors,
        List<SimplifiedPostResponse> posts,
        int size,
        PostCursor nextCursor,
        long totalElements,
        boolean isLast,
        boolean isInverted
) {
    public static CursorBasedPostsResponse from(
            CursorPage<Post, PostCursor> posts,
            Map<Long, PersonalizedPostReactionDto> personalizedPostReactions,
            List<ChannelMemberDto> authors
    ) {
        List<SimplifiedPostResponse> simplifiedPosts = posts.getContent().stream()
                .map(post -> {
                    List<ImageDto> images = post.getPostImages().stream()
                            .map(postImage -> new ImageDto(postImage.getImage().getId(), postImage.getImage().getUrl()))
                            .toList();
                    PersonalizedPostReactionDto personalizedReaction = personalizedPostReactions.get(post.getId());
                    return new SimplifiedPostResponse(
                            post.getId(),
                            post.getContent(),
                            images,
                            PostMetricResponse.from(post.getMetric()),
                            personalizedReaction.likedByRequester(),
                            personalizedReaction.dislikedByRequester(),
                            post.getAuthor().getId(),
                            post.getCreatedAt(),
                            post.getModifiedAt()
                    );
                })
                .toList();

        return new CursorBasedPostsResponse(
                authors,
                simplifiedPosts,
                posts.getSize(),
                posts.getNextCursor(),
                posts.getTotalElements(),
                posts.isLast(),
                posts.isInverted()
        );
    }
}
