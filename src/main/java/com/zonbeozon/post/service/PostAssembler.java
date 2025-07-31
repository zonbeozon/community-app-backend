package com.zonbeozon.post.service;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.channel.security.MemberOfChannelOnly;
import com.zonbeozon.channel.service.ChannelMemberAssembler;
import com.zonbeozon.channel.service.BlogChannelFinder;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import com.zonbeozon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostAssembler {
    private final PostRepository postRepository;
    private final BlogChannelFinder blogChannelFinder;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final PostFinder postFinder;
    private final PostImageRepository postImageRepository;
    private final ChannelMemberFinder channelMemberFinder;

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
        return CursorBasedPostsResponse.from(authorResponse, posts);
    }

    public PostResponse createPostResponse(Long postId) {
        Post post = postFinder.findById(postId);
        List<PostImage> postImages = postImageRepository.findAllByPostId(postId);
        ChannelMember author = channelMemberFinder.findByMemberAndChannel(post.getAuthor(), post.getChannel());
        return PostResponse.from(post, author, postImages);
    }

    private List<Member> getDistinctAuthorsFromPosts(List<Post> posts) {
        return posts.stream().map(Post::getAuthor).distinct().toList();
    }
}
