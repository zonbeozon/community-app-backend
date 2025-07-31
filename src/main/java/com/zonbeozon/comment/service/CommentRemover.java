package com.zonbeozon.comment.service;

import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.dto.CommentDeletedEvent;
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

    @CheckChannelAccess(ChannelAction.CHANNEL_DELETE)
    public void deleteComment(Long commentId) {
        Comment comment = commentFinder.findById(commentId);
        comment.deleteComment();
        eventPublisher.publishEvent(new CommentDeletedEvent(
                comment.getPost().getId(),
                comment.getId())
        );
    }
}
