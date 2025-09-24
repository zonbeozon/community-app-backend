package com.zonbeozon.comment.repository;

import com.zonbeozon.comment.dto.CommentCountResult;
import com.zonbeozon.comment.dto.CommentDto;
import com.zonbeozon.comment.dto.CommentWithAuthorResponse;
import com.zonbeozon.comment.entity.Comment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    List<CommentDto> findCommentDtoByPostIdWOrderByCreatedAtDesc(Long postId);
    List<CommentCountResult> countCommentsByPostIds(Collection<Long> postIds);
    Optional<Comment> findById(Long id, CommentFetchOptions options);
    Optional<CommentWithAuthorResponse> findCommentWithAuthorByCommentId(Long commentId);
}
