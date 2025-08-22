package com.zonbeozon.comment.service;

import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.dto.CommentDeletedEvent;
import com.zonbeozon.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentRemover {
    private final CommentFinder commentFinder;
    private final ApplicationEventPublisher eventPublisher;
    private final CommentRepository commentRepository;

    public void deleteComment(Long commentId) {
        Comment comment = commentFinder.findByIdElseThrow(commentId);
        commentRepository.delete(comment);
        eventPublisher.publishEvent(new CommentDeletedEvent(
                comment.getPost().getId(),
                comment.getId())
        );
    }
}
