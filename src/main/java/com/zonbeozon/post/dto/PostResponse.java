package com.zonbeozon.post.dto;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.image.entity.ImageResponse;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        long postId,
        String content,
        List<ImageResponse> images,
        ChannelMemberResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post, ChannelMember author, List<PostImage> postImages) {
        List<ImageResponse> imageResponse = postImages.isEmpty() ? List.of() : postImages.stream().map(PostImage::getImage).map(ImageResponse::from).toList();
        return new PostResponse(
                post.getId(),
                post.getContent(),
                imageResponse,
                ChannelMemberResponse.from(author),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
