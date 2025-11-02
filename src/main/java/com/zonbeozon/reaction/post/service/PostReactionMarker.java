package com.zonbeozon.reaction.post.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.service.PostFinder;
import com.zonbeozon.reaction.post.entity.PostReaction;
import com.zonbeozon.reaction.post.entity.ReactionType;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class PostReactionMarker implements ReactionMarkHandler, ReactionUnmarkHandler {
    private final PostFinder postFinder;
    private final MemberFinder memberFinder;
    private final PostReactionRepository postReactionRepository;

    @Override
    public void mark(Long requesterId, Long postId, ReactionType reactionType) {
        Member requester = memberFinder.findByIdElseThrow(requesterId);
        Post post = postFinder.findByIdElseThrow(postId);
        //이미 해당 post에 대해 리엑션이 있다면 기존 리엑션을 삭제
        postReactionRepository.findByPostAndAuthor(post, requester)
                .ifPresent(postReactionRepository::delete);

        PostReaction reaction = PostReaction.create(post, reactionType, requester);
        post.getReactions().add(reaction);
        postReactionRepository.save(reaction);
    }

    @Override
    public void unmark(Long requesterId, Long postId) {
        Member requester = memberFinder.findByIdElseThrow(requesterId);
        Post post = postFinder.findByIdElseThrow(postId);
        PostReaction reaction = postReactionRepository.findByPostAndAuthor(post, requester)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REACTION_NOT_FOUND));
        post.getReactions().remove(reaction);
        postReactionRepository.delete(reaction);
    }
}
