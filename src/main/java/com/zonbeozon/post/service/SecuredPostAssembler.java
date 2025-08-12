package com.zonbeozon.post.service;

import com.zonbeozon.channel.security.MemberOfChannelOnly;
import com.zonbeozon.channel.service.BlogChannelFinder;
import com.zonbeozon.channel.service.ChannelMemberAssembler;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.comment.service.CommentCounter;
import com.zonbeozon.post.dto.CursorBasedPostsResponse;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.service.PostReactionAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class SecuredPostAssembler extends PostAssembler {

    @Autowired
    public SecuredPostAssembler(PostRepository postRepository, BlogChannelFinder blogChannelFinder, ChannelMemberAssembler channelMemberAssembler, PostFinder postFinder, ChannelMemberFinder channelMemberFinder, CommentCounter commentCounter, PostReactionAssembler postReactionAssembler) {
        super(postRepository, blogChannelFinder, channelMemberAssembler, postFinder, channelMemberFinder, commentCounter, postReactionAssembler);
    }

    @Override
    @MemberOfChannelOnly
    public CursorBasedPostsResponse createCursorBasedPostResponse(Long channelId, Long cursorPostId, int size) {
        return super.createCursorBasedPostResponse(channelId, cursorPostId, size);
    }

    @Override
    @MemberOfChannelOnly(evaluateBy = "postId")
    public PostResponse createPostResponse(Long postId) {
        return super.createPostResponse(postId);
    }
}
