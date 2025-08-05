package com.zonbeozon.post.service;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.channel.security.MemberOfChannelOnly;
import com.zonbeozon.channel.service.ChannelMemberAssembler;
import com.zonbeozon.channel.service.BlogChannelFinder;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.comment.dto.CommentCountResult;
import com.zonbeozon.comment.service.CommentCounter;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.CursorPageImpl;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.dto.PostWithStats;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.post.repository.PostFetchOptions;
import com.zonbeozon.post.repository.PostImageRepository;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.dto.ReactionResponse;
import com.zonbeozon.reaction.service.PostReactionAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostAssembler {
    private final PostRepository postRepository;
    private final BlogChannelFinder blogChannelFinder;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final PostFinder postFinder;
    private final ChannelMemberFinder channelMemberFinder;
    private final CommentCounter commentCounter;
    private final PostReactionAssembler postReactionAssembler;

    @MemberOfChannelOnly
    public CursorBasedPostsResponse createCursorBasedPostResponse(
            Long channelId,
            Long cursorPostId,
            int size
    ) {
        BlogChannel channel = blogChannelFinder.findById(channelId);
        CursorPage<Post> posts = postRepository.findCursorBasedPostsByChannel(channel, cursorPostId, size);
        List<ChannelMemberResponse> authorResponse = channelMemberAssembler.createChannelMemberListResponse(
                getDistinctAuthorsFromPosts(posts.getContent()),
                channel
        );
        List<Long> postIds = posts.getContent().stream().map(Post::getId).toList();
        Map<Long, Long> commentCountResult = commentCounter.countCommentsByPostIdIn(postIds);
        Map<Long, ReactionResponse> reactionResponseMap = postReactionAssembler.createReactionResponseByPostIdIn(postIds);

        CursorPage<PostWithStats> postWithStats = posts.map(post -> {
            Long commentCount = commentCountResult.get(post.getId());
            ReactionResponse reactionResponse = reactionResponseMap.get(post.getId());
            return new PostWithStats(post, commentCount, reactionResponse);
        });

        return CursorBasedPostsResponse.from(authorResponse, postWithStats);
    }

    public PostResponse createPostResponseByPostId(Long postId) {
        Post post = postFinder.findById(
                postId,
                new PostFetchOptions.Builder().withImages(true).withAuthor(true).build()
        );
        ChannelRole authorRole = channelMemberFinder.findByMemberAndChannel(post.getAuthor(), post.getChannel()).getRole();
        Long commentCount = commentCounter.countCommentsByPostId(postId);
        ReactionResponse reactionResponse = postReactionAssembler.createReactionResponseByPostId(postId);
        return PostResponse.from(post, commentCount, reactionResponse, authorRole);
    }

    private List<Member> getDistinctAuthorsFromPosts(List<Post> posts) {
        return posts.stream().map(Post::getAuthor).distinct().toList();
    }

}
