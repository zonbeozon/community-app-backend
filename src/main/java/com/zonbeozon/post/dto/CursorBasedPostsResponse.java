package com.zonbeozon.post.dto;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.post.domain.Post;

import java.util.List;

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
            List<ChannelMemberDto> authors
    ) {
        List<SimplifiedPostResponse> simplifiedPosts = posts.getContent().stream()
                .map(post -> {
                    List<ImageDto> images = post.getPostImages().stream()
                            .map(postImage -> new ImageDto(postImage.getImage().getId(), postImage.getImage().getUrl()))
                            .toList();
                    return new SimplifiedPostResponse(
                        post.getId(),
                        post.getContent(),
                        images,
                        post.getAuthor().getId(),
                        post.getMetric().getViewCount(),
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
