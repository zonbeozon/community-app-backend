package com.zonbeozon.post.dto;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.comment.dto.CommentResponse;
import com.zonbeozon.image.entity.ImageResponse;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.reaction.dto.ReactionResponse;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        long postId,
        String content,
        List<ImageResponse> images,
        ChannelMemberResponse author,
        Long commentCount,
        ReactionResponse reaction,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(PostWithStats postWithStats, ChannelMember author) {
        Post post = postWithStats.getPost();
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getImages().stream().map(PostImage::getImage).map(ImageResponse::from).toList(),
                ChannelMemberResponse.from(author),
                postWithStats.getCommentCount(),
                postWithStats.getReactionResponse(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }

    public static PostResponse from(Post post, Long commentCount, ReactionResponse reactionResponse, ChannelRole authorRole) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getImages().stream().map(PostImage::getImage).map(ImageResponse::from).toList(),
                ChannelMemberResponse.from(post.getAuthor(), authorRole),
                commentCount,
                reactionResponse,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
