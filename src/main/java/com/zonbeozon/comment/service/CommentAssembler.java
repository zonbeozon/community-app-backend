package com.zonbeozon.comment.service;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.channel.service.ChannelMemberFinder;
import com.zonbeozon.comment.dto.CommentsWithAuthorResponse;
import com.zonbeozon.comment.dto.CommentResponse;
import com.zonbeozon.comment.dto.SimplifiedCommentResponse;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentFetchOptions;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostFetchOptions;
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

    public CommentsWithAuthorResponse getCommentResponseByPostId(Long postId) {
        Post post = postFinder.findByIdElseThrow(postId, new PostFetchOptions.Builder().withChannel(true).build());
        List<Comment> comments = commentRepository.findCommentsByPostIdOrderByCreatedAtDesc(postId);
        List<SimplifiedCommentResponse> commentResponse = comments.stream()
                .map(SimplifiedCommentResponse::from)
                .toList();
        List<ChannelMemberResponse> authorResponse = channelMemberAssembler.getChannelMemberResponse(
                comments.stream()
                        .map(Comment::getAuthor)
                        .distinct()
                        .map(author-> ChannelMemberId.from(post.getChannel(), author))
                        .toList()
        ).values().stream().toList();
        return new CommentsWithAuthorResponse(authorResponse, commentResponse, comments.size());
    }

    public CommentResponse createCommentResponse(Long commentId) {
        Comment comment = commentFinder.findByIdElseThrow(commentId, new CommentFetchOptions.Builder().withAuthor(true).withPost(true).build());
        ChannelMember author = channelMemberFinder.findByIdElseThrow(ChannelMemberId.from(comment.getPost().getChannel(), comment.getAuthor()));
        return CommentResponse.from(comment, author);
    }
}
