package com.zonbeozon.post.service.metric;

import com.zonbeozon.comment.dto.CommentCreatedEvent;
import com.zonbeozon.comment.dto.CommentDeletedEvent;

import com.zonbeozon.post.repository.PostMetricRepository;
import com.zonbeozon.reaction.post.dto.PostReactionAddEvent;
import com.zonbeozon.reaction.post.dto.PostReactionUnmarkEvent;
import com.zonbeozon.reaction.post.dto.PostReactionUpdateEvent;
import com.zonbeozon.reaction.post.entity.ReactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CountUpdater {
    private final PostMetricRepository postMetricRepository;

    @EventListener(PostReactionAddEvent.class)
    public void handleReactionAdd(PostReactionAddEvent event) {
        if(event.reactionType() == ReactionType.LIKE)
            postMetricRepository.updateLikeCount(event.postId(), 1L);
        else if(event.reactionType() == ReactionType.DISLIKE)
            postMetricRepository.updateDislikeCount(event.postId(), 1L);
    }

    @EventListener(PostReactionUpdateEvent.class)
    public void handleReactionUpdate(PostReactionUpdateEvent event) {
        if(event.updateTo() == ReactionType.LIKE) {
            postMetricRepository.updateLikeCount(event.postId(), 1L);
            postMetricRepository.updateDislikeCount(event.postId(), -1L);
        }
        else if(event.updateTo() == ReactionType.DISLIKE) {
            postMetricRepository.updateLikeCount(event.postId(), -1L);
            postMetricRepository.updateDislikeCount(event.postId(), 1L);
        }
    }

    @EventListener(PostReactionUnmarkEvent.class)
    public void handleReactionUnmark(PostReactionUnmarkEvent event) {
        if(event.reactionType() == ReactionType.LIKE)
            postMetricRepository.updateLikeCount(event.postId(), -1L);
        else if(event.reactionType() == ReactionType.DISLIKE)
            postMetricRepository.updateDislikeCount(event.postId(), -1L);
    }

    @EventListener(CommentCreatedEvent.class)
    public void handleCommentCreated(CommentCreatedEvent event) {
           postMetricRepository.updateCommentCount(event.postId(), 1L);
    }

    @EventListener(CommentDeletedEvent.class)
    public void handleCommentDeleted(CommentDeletedEvent event) {
        postMetricRepository.updateCommentCount(event.postId(), -1L);
    }
}
