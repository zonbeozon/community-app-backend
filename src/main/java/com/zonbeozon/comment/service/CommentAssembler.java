package com.zonbeozon.comment.service;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.comment.dto.CommentWithAuthorResponse;
import com.zonbeozon.comment.dto.CommentsWithAuthorResponse;
import com.zonbeozon.comment.dto.CommentDto;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
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
    private final ChannelMemberAssembler channelMemberAssembler;

    public CommentsWithAuthorResponse getCommentResponseByPostId(Long postId) {
        Post post = postFinder.findByIdElseThrow(postId);
        List<CommentDto> comments = commentRepository.findCommentDtoByPostIdWOrderByCreatedAtDesc(postId);
        List<Long> authorIds = comments.stream()
                .map(CommentDto::authorId)
                .distinct()
                .toList();
        List<ChannelMemberDto> authorResponse = channelMemberAssembler.getChannelMembers(post.getChannel().getId(), authorIds);

        return new CommentsWithAuthorResponse(
                authorResponse,
                comments,
                comments.size());
    }

    public CommentWithAuthorResponse getCommentResponse(Long commentId) {
        return commentRepository.findCommentWithAuthorByCommentId(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
