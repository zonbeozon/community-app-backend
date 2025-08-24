package com.zonbeozon.comment.service;

import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentFetchOptions;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentFinder {
    private final CommentRepository commentRepository;

    public Comment findByIdElseThrow(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public Comment findByIdElseThrow(Long id, CommentFetchOptions options) {
        return commentRepository.findById(id, options).orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public Comment findByIdWithPostElseThrow(Long id) {
        return commentRepository.findByIdWithPost(id).orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
