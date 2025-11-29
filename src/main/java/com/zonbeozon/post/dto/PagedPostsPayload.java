package com.zonbeozon.post.dto;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;

import java.util.List;
import java.util.Map;

public record PagedPostsPayload(
        List<PostPayload> posts,
        int size,
        PostCursor nextCursor,
        long totalElements,
        boolean isLast,
        boolean isInverted
) {
    public static PagedPostsPayload from(
            CursorPage<Post, PostCursor> pagedPosts,
            Map<Long, PersonalizedPostReactionDto> personalizedPostReactions,
            Map<Long, ChannelMemberDto> authors
    ) {
        List<PostPayload> posts = pagedPosts.getContent().stream()
                .map(post -> PostPayload.from(post, personalizedPostReactions.get(post.getId()), authors.get(post.getAuthor().getId())))
                .toList();

        return new PagedPostsPayload(
                posts,
                pagedPosts.getSize(),
                pagedPosts.getNextCursor(),
                pagedPosts.getTotalElements(),
                pagedPosts.isLast(),
                pagedPosts.isInverted()
        );
    }
}
