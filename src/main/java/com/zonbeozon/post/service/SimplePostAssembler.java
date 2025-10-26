package com.zonbeozon.post.service;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.channel.service.finder.BlogChannelFinder;
import com.zonbeozon.comment.service.CommentCounter;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostFetchOptions;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.post.service.PostReactionAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimplePostAssembler implements PostAssembler {
    private final PostRepository postRepository;
    private final BlogChannelFinder blogChannelFinder;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final PostFinder postFinder;
    private final CommentCounter commentCounter;
    private final PostImageService postImageService;

    public CursorBasedPostsResponse getCursorBasedPostResponse(
            Long channelId,
            PostCursor cursor,
            int size,
            boolean inverted
    ) {
        blogChannelFinder.findByIdElseThrow(channelId);
        CursorPage<Post, PostCursor> pagedPosts = postRepository.findCursorBasedPostsByChannelId(channelId, cursor, size, inverted);
        postImageService.loadImages(pagedPosts.getContent());
        List<ChannelMemberDto> authorResponse = channelMemberAssembler.getChannelMembers(
                channelId,
                getDistinctAuthorIdsFromPosts(pagedPosts.getContent())
        );
        return CursorBasedPostsResponse.from(pagedPosts, authorResponse);
    }

    public PostResponse getPostResponse(@Nullable Long requesterId, Long postId) {
        Post post = postFinder.findByIdElseThrow(
                postId,
                new PostFetchOptions.Builder().withImages(true).withAuthor(true).withChannel(true).build()
        );
        ChannelMemberDto authorResponse = channelMemberAssembler.getChannelMember(post.getChannel().getId(), post.getAuthor().getId());
        Long commentCount = commentCounter.countCommentsByPostId(postId);
        return PostResponse.from(post, commentCount, authorResponse);
    }

    private List<Long> getDistinctAuthorIdsFromPosts(List<Post> posts) {
        return posts.stream().map(Post::getAuthor).map(Member::getId).distinct().toList();
    }
}
