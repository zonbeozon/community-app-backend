package com.zonbeozon.post.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.CheckReturnValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostAuthorizationCheckService {
    private final AuthenticationService authenticationService;
    private final PostFinder postFinder;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;
    private final PostRepository postRepository;

    @CheckReturnValue
    public boolean isAuthorOrHasHigherRoleThanAuthor(Long postId) {
        Member actor = authenticationService.getCurrentMember();
        Post post = postFinder.findByIdElseThrow(postId);
        return actor.getId().equals(post.getAuthor().getId()) || channelAuthorizationCheckService.hasHigherRoleThanTargetMember(post.getChannel().getId(), post.getAuthor().getId());
    }

    @CheckReturnValue
    public boolean isAuthor(Long postId) {
        Member actor = authenticationService.getCurrentMember();
        Post post = postFinder.findByIdElseThrow(postId);
        return actor.getId().equals(post.getAuthor().getId());
    }

    @CheckReturnValue
    public boolean canAccessChannelContent(Long postId) {
        Long channelId = postFinder.findByIdElseThrow(postId).getChannel().getId();
        return channelAuthorizationCheckService.canAccessChannelContent(channelId);
    }

    @CheckReturnValue
    public boolean isAtLeastMember(Long postId) {
        Long channelId = postFinder.findByIdElseThrow(postId).getChannel().getId();
        return channelAuthorizationCheckService.isAtLeastMember(channelId);
    }

    @CheckReturnValue
    public boolean isAtLeastMember(Long postId, Long memberId) {
        Long channelId = postFinder.findByIdElseThrow(postId).getChannel().getId();;
        return channelAuthorizationCheckService.isAtLeastMember(channelId, memberId);
    }

    public void validatePostsInChannel(Long channelId, Collection<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return;
        }
        long validPostCount = postRepository.countPostsInChannel(postIds, channelId);

        if (validPostCount != postIds.size()) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }
}
