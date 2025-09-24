package com.zonbeozon.post.service;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.channel.service.finder.BlogChannelFinder;
import com.zonbeozon.comment.service.CommentCounter;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.LongTypeCursorPage;
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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
            @Nullable Long requesterId,
            Long channelId,
            Long cursorPostId,
            int size,
            boolean inverted
    ) {
        if(!blogChannelFinder.existsById(channelId)) throw new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
        LongTypeCursorPage<Post> posts = postRepository.findCursorBasedPostsByChannelId(channelId, cursorPostId, size, inverted);
        List<ChannelMemberDto> authorResponse = channelMemberAssembler.getChannelMembers(channelId, getDistinctAuthorIdsFromPosts(posts.getContent()));
        List<Long> postIds = posts.getContent().stream().map(Post::getId).toList();
        Map<Long, Long> commentCounts = commentCounter.countCommentsByPostIdIn(postIds);

        LongTypeCursorPage<PostWithStats> postWithStats;
        if(requesterId != null) {
            Map<Long, ReactionResponse> reactionResponseMap = postReactionAssembler.getReactionResponseByPostIdIn(requesterId, postIds);
            postWithStats = posts.map(post -> {
                Long commentCount = commentCounts.get(post.getId());
                ReactionResponse reactionResponse = reactionResponseMap.get(post.getId());
                return new PostWithStats(post, commentCount, reactionResponse);
            });
        } else {
            postWithStats = posts.map(post -> {
                Long commentCount = commentCounts.get(post.getId());
                return new PostWithStats(post, commentCount, null);
            });
        }
        return CursorBasedPostsResponse.from(authorResponse, postWithStats);
    }

    public PostResponse getPostResponse(@Nullable Long requesterId, Long postId) {
        Post post = postFinder.findByIdElseThrow(
                postId,
                new PostFetchOptions.Builder().withImages(true).withAuthor(true).withChannel(true).build()
        );
        ChannelMemberDto authorResponse = channelMemberAssembler.getChannelMember(post.getChannel().getId(), post.getAuthor().getId());
        Long commentCount = commentCounter.countCommentsByPostId(postId);

        ReactionResponse reactionResponse = requesterId == null ?
                null : postReactionAssembler.getReactionResponseByPostId(requesterId, postId);
        return PostResponse.from(post, commentCount, reactionResponse, authorResponse);
    }

    private List<Long> getDistinctAuthorIdsFromPosts(List<Post> posts) {
        return posts.stream().map(Post::getAuthor).map(Member::getId).distinct().toList();
    }
}
