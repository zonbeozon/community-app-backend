package com.zonbeozon.comment.service;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.security.MemberOfChannelOnly;
import com.zonbeozon.channel.service.ChannelMemberAssembler;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.comment.dto.CommentListResponse;
import com.zonbeozon.comment.dto.CommentResponse;
import com.zonbeozon.comment.dto.SimplifiedCommentResponse;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAssembler {
    private final CommentRepository commentRepository;
    private final PostFinder postFinder;
    private final CommentFinder commentFinder;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final ChannelMemberFinder channelMemberFinder;

    @MemberOfChannelOnly(evaluateBy = "postId")
    public CommentListResponse createCommentListResponse(Long postId) {
        Post post = postFinder.findById(postId);
        List<Comment> comments = commentRepository.getCommentsByPostIdOrderByCreatedAtDesc(postId);
        List<SimplifiedCommentResponse> commentResponse = comments.stream()
                .map(SimplifiedCommentResponse::from)
                .toList();
        List<Member> authors = comments.stream().map(Comment::getAuthor).distinct().toList();
        List<ChannelMemberResponse> authorResponse = channelMemberAssembler.createChannelMemberListResponse(authors, post.getChannel());
        return new CommentListResponse(authorResponse, commentResponse, comments.size());
    }

    public CommentResponse createCommentResponse(Long commentId) {
        Comment comment = commentFinder.findById(commentId);
        ChannelMember author = channelMemberFinder.findByMemberAndChannel(comment.getAuthor(), comment.getPost().getChannel());
        return CommentResponse.from(comment, author);
    }
}
