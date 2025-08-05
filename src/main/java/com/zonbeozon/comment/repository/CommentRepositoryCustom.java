package com.zonbeozon.comment.repository;

import com.zonbeozon.comment.dto.CommentCountResult;
import com.zonbeozon.comment.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> getCommentsByPostIdOrderByCreatedAtDesc(Long postId);
    List<CommentCountResult> countCommentsByPostIds(List<Long> postIds);
}
