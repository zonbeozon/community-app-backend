package com.zonbeozon.reaction.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostFinder;
import com.zonbeozon.reaction.dto.ReactionResponse;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.enums.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReactionAssembler {
    private final PostReactionRepository postReactionRepository;
    private final PostFinder postFinder;
    private final AuthenticationService authenticationService;

    public Map<Long, ReactionResponse> createReactionResponseByPostIdIn(List<Long> postIds) {
        Member requester = authenticationService.getCurrentMember();
        List<Post> posts = postFinder.findByIdIn(postIds);
        List<PostReaction> allReactions = postReactionRepository.findByPostIn(posts);

        List<PostReaction> reactionsByRequester = postReactionRepository.findByPostInAndAuthor(posts, requester);

        Map<Long, List<PostReaction>> reactionsByPostId = allReactions.stream()
                .collect(Collectors.groupingBy(reaction -> reaction.getPost().getId()));

        Map<Long, PostReaction> reactionByRequesterByPostId = reactionsByRequester.stream()
                .collect(Collectors.toMap(reaction -> reaction.getPost().getId(), reaction -> reaction));

        return posts.stream()
                .collect(Collectors.toMap(Post::getId, post -> {
                    List<PostReaction> currentPostReactions = reactionsByPostId.getOrDefault(post.getId(), Collections.emptyList());

                    Map<ReactionType, Long> reactionCounts = currentPostReactions.stream()
                            .collect(Collectors.groupingBy(
                                    PostReaction::getReactionType,
                                    Collectors.counting()
                            ));

                    PostReaction requesterReaction = reactionByRequesterByPostId.get(post.getId());

                    boolean likedByCurrentUser = false;
                    boolean dislikedByCurrentUser = false;

                    if (requesterReaction != null) {
                        likedByCurrentUser = requesterReaction.getReactionType() == ReactionType.LIKE;
                        dislikedByCurrentUser = requesterReaction.getReactionType() == ReactionType.DISLIKE;
                    }

                    return new ReactionResponse(reactionCounts, likedByCurrentUser, dislikedByCurrentUser);
                }));
    }

    public ReactionResponse createReactionResponseByPostId(Long postId) {
        return createReactionResponseByPostIdIn(List.of(postId)).get(postId);
    }

}
