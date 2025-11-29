package com.zonbeozon.reaction.post.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.service.PostFinder;
import com.zonbeozon.reaction.post.dto.PostReactionAddEvent;
import com.zonbeozon.reaction.post.dto.PostReactionUnmarkEvent;
import com.zonbeozon.reaction.post.dto.PostReactionUpdateEvent;
import com.zonbeozon.reaction.post.entity.PostReaction;
import com.zonbeozon.reaction.post.entity.ReactionType;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class PostReactionMarker implements ReactionMarkHandler, ReactionUnmarkHandler {
    private final PostFinder postFinder;
    private final MemberFinder memberFinder;
    private final PostReactionRepository postReactionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void mark(Long requesterId, Long postId, ReactionType reactionType) {
        Member requester = memberFinder.findByIdElseThrow(requesterId);
        Post post = postFinder.findByIdElseThrow(postId);
        //이미 해당 post에 대해 리엑션이 있다면 기존 리엑션을 삭제
        Optional<PostReaction> optReaction = postReactionRepository.findByPostAndAuthor(post, requester);
        if(optReaction.isPresent()) {
            PostReaction reaction = optReaction.get();
            updateReaction(reaction, reactionType);
            return;
        }
        PostReaction reaction = PostReaction.create(post, reactionType, requester);
        postReactionRepository.save(reaction);
        eventPublisher.publishEvent(new PostReactionAddEvent(postId, reactionType));
    }

    private void updateReaction(PostReaction reaction, ReactionType typeWantToChange) {
        if(reaction.getReactionType() == typeWantToChange) return;
        reaction.setReactionType(typeWantToChange);
        eventPublisher.publishEvent(new PostReactionUpdateEvent(reaction.getPost().getId(), typeWantToChange));
    }

    @Override
    public void unmark(Long requesterId, Long postId) {
        Member requester = memberFinder.findByIdElseThrow(requesterId);
        Post post = postFinder.findByIdElseThrow(postId);
        PostReaction reaction = postReactionRepository.findByPostAndAuthor(post, requester)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REACTION_NOT_FOUND));
        post.getReactions().remove(reaction);
        postReactionRepository.delete(reaction);
        eventPublisher.publishEvent(new PostReactionUnmarkEvent(postId, reaction.getReactionType()));
    }
}
