package com.zonbeozon.post.service;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;
import com.zonbeozon.reaction.post.service.PostReactionAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimplePostAssembler implements PostAssembler {
    private final PostRepository postRepository;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final PostReactionAssembler postReactionAssembler;
    private final PostImageService postImageService;
    private final ChannelFinder channelFinder;

    public CursorBasedPostsResponse getCursorBasedPostResponse(
            Long requesterId,
            Long channelId,
            PostCursor cursor,
            int size,
            boolean inverted
    ) {
        channelFinder.findByIdElseThrow(channelId);
        CursorPage<Post, PostCursor> pagedPosts = postRepository.searchByChannelIdWithMetric(channelId, cursor, size, inverted);
        postImageService.loadImages(pagedPosts.getContent());
        List<ChannelMemberDto> authorResponse = channelMemberAssembler.getChannelMembers(
                channelId,
                extractDistinctAuthorIdsFromPosts(pagedPosts.getContent())
        );
        Map<Long, PersonalizedPostReactionDto> personalizedReactions = postReactionAssembler.getPersonalizedInfoByPostIdIn(requesterId, extractPostId(pagedPosts.getContent()));
        return CursorBasedPostsResponse.from(pagedPosts, personalizedReactions, authorResponse);
    }

    public PostResponse getPostResponse(Long requesterId, Long postId) {
        Post post = postRepository.findByIdWithChannelAndImagesAndMetric(postId).orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        ChannelMemberDto authorResponse = channelMemberAssembler.getChannelMember(post.getChannel().getId(), post.getAuthor().getId());
        PersonalizedPostReactionDto personalizedReaction = postReactionAssembler.getPersonalizedInfoByPostId(requesterId, postId);
        return PostResponse.from(post, personalizedReaction.likedByRequester(), personalizedReaction.dislikedByRequester(), authorResponse);
    }

    public PostResponse getPostResponse(Long postId) {
        Post post = postRepository.findByIdWithChannelAndImagesAndMetric(postId).orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        ChannelMemberDto authorResponse = channelMemberAssembler.getChannelMember(post.getChannel().getId(), post.getAuthor().getId());
        return PostResponse.from(post, false, false, authorResponse);
    }

    private List<Long> extractDistinctAuthorIdsFromPosts(List<Post> posts) {
        return posts.stream().map(Post::getAuthor).map(Member::getId).distinct().toList();
    }

    private List<Long> extractPostId(List<Post> posts) {
        return posts.stream().map(Post::getId).toList();
    }
}
