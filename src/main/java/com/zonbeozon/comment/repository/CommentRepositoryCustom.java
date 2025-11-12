package com.zonbeozon.comment.repository;

import com.zonbeozon.comment.dto.CommentDto;
import com.zonbeozon.comment.dto.CommentWithAuthorResponse;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryCustom {
    List<CommentDto> findCommentDtoByPostIdWOrderByCreatedAtDesc(Long postId);
    Optional<CommentWithAuthorResponse> findCommentWithAuthorByCommentId(Long commentId);
}
