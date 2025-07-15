package com.zonbeozon.reaction.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.security.MemberOfChannelOnly;
import com.zonbeozon.channel.security.SimpleChannelPermissionEvaluator;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostFinder;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.enums.ReactionContentType;
import com.zonbeozon.reaction.enums.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class PostReactionHandler implements ReactionMarkHandler, ReactionUnmarkHandler {
    private final PostFinder postFinder;
    private final AuthenticationService authenticationService;
    private final PostReactionRepository postReactionRepository;
    private final SimpleChannelPermissionEvaluator channelRoleBasedPermissionEvaluator;

    @Override
    @MemberOfChannelOnly
    public void mark(Long contentId, ReactionType reactionType) {
        Member requester = authenticationService.getCurrentMember();
        Post post = postFinder.findById(contentId);
        //이미 해당 post에 대해 리엑션이 있다면 기존 리엑션을 삭제
        postReactionRepository.findByPostAndAuthor(post, requester)
                .ifPresent(postReactionRepository::delete);

        PostReaction reaction = PostReaction.create(post, reactionType, requester);
        postReactionRepository.save(reaction);
    }

    @Override
    @MemberOfChannelOnly
    public void unmark(Long contentId) {
        Member requester = authenticationService.getCurrentMember();
        Post post = postFinder.findById(contentId);
        PostReaction reaction = postReactionRepository.findByPostAndAuthor(post, requester)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REACTION_NOT_FOUND));
        postReactionRepository.delete(reaction);
    }

    @Override
    public boolean isSupport(ReactionContentType reactionContentType) {
        return reactionContentType == ReactionContentType.POST;
    }
}
