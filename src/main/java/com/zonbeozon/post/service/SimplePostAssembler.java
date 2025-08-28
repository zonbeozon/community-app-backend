package com.zonbeozon.post.service;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.channel.service.BlogChannelFinder;
import com.zonbeozon.comment.service.CommentCounter;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.dto.PostWithStats;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostFetchOptions;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.dto.ReactionResponse;
import com.zonbeozon.reaction.service.PostReactionAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimplePostAssembler implements PostAssembler {
    private final PostRepository postRepository;
    private final BlogChannelFinder blogChannelFinder;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final PostFinder postFinder;
    private final CommentCounter commentCounter;
    private final PostReactionAssembler postReactionAssembler;

    public CursorBasedPostsResponse getCursorBasedPostResponse(
            Long channelId,
            Long cursorPostId,
            int size,
            boolean inverted
    ) {
        if(!blogChannelFinder.existsById(channelId)) throw new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
        CursorPage<Post> posts = postRepository.findCursorBasedPostsByChannelId(channelId, cursorPostId, size, inverted);
        List<ChannelMemberResponse> authorResponse = channelMemberAssembler.getChannelMemberResponse(
                getDistinctAuthorIdsFromPosts(posts.getContent()).stream()
                        .map(authorId -> new ChannelMemberId(channelId, authorId)).toList()
        ).values().stream().toList();
        List<Long> postIds = posts.getContent().stream().map(Post::getId).toList();
        Map<Long, Long> commentCounts = commentCounter.countCommentsByPostIdIn(postIds);
        Map<Long, ReactionResponse> reactionResponseMap = postReactionAssembler.getReactionResponseByPostIdIn(postIds);

        CursorPage<PostWithStats> postWithStats = posts.map(post -> {
            Long commentCount = commentCounts.get(post.getId());
            ReactionResponse reactionResponse = reactionResponseMap.get(post.getId());
            return new PostWithStats(post, commentCount, reactionResponse);
        });
        return CursorBasedPostsResponse.from(authorResponse, postWithStats);
    }

    public PostResponse getPostResponse(Long postId) {
        Post post = postFinder.findByIdElseThrow(
                postId,
                new PostFetchOptions.Builder().withImages(true).withAuthor(true).build()
        );
        ChannelMemberResponse authorResponse = channelMemberAssembler.getChannelMemberResponse(new ChannelMemberId(post.getChannel().getId(), post.getAuthor().getId()));
        Long commentCount = commentCounter.countCommentsByPostId(postId);
        ReactionResponse reactionResponse = postReactionAssembler.getReactionResponseByPostId(postId);
        return PostResponse.from(post, commentCount, reactionResponse, authorResponse);
    }

    @Override
    public Map<Long, PostResponse> getPostResponses(Collection<Long> postIds) {
        Map<Long, Post> posts = postRepository.findByIdInWithImagesAndAuthorAndChannel(postIds).stream().collect(Collectors.toMap(
                Post::getId,
                Function.identity()
        ));
        Map<ChannelMemberId, ChannelMemberResponse> authorResponses = channelMemberAssembler.getChannelMemberResponse(
                posts.values().stream().map(post -> new ChannelMemberId(post.getChannel().getId(), post.getAuthor().getId())).toList()
        );
        Map<Long, Long> commentCounts = commentCounter.countCommentsByPostIdIn(postIds);
        Map<Long, ReactionResponse> reactionResponses = postReactionAssembler.getReactionResponseByPostIdIn(postIds);

        return postIds.stream().map(postId -> {
            Post post = posts.get(postId);
            return PostResponse.from(
                    post,
                    commentCounts.get(postId),
                    reactionResponses.get(postId),
                    authorResponses.get(new ChannelMemberId(post.getChannel().getId(), post.getAuthor().getId())));
        }).collect(Collectors.toMap(PostResponse::postId, Function.identity()));
    }

    private List<Long> getDistinctAuthorIdsFromPosts(List<Post> posts) {
        return posts.stream().map(Post::getAuthor).map(Member::getId).distinct().toList();
    }
}
