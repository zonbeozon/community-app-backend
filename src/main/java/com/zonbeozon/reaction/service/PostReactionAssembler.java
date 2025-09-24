package com.zonbeozon.reaction.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostFinder;
import com.zonbeozon.reaction.dto.ReactionResponse;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.enums.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReactionAssembler {
    private final PostReactionRepository postReactionRepository;
    private final PostFinder postFinder;
    private final MemberFinder memberFinder;

    public Map<Long, ReactionResponse> getReactionResponseByPostIdIn(Long requesterId, Collection<Long> postIds) {
        Member requester = memberFinder.findByIdElseThrow(requesterId);
        List<Post> posts = postFinder.findByIdIn(postIds);
        List<PostReaction> allReactions = postReactionRepository.findByPostIn(posts);

        Map<Long, PostReaction> reactionByRequesterByPostId = postReactionRepository.findByPostInAndAuthor(posts, requester).stream()
                    .collect(Collectors.toMap(reaction -> reaction.getPost().getId(), reaction -> reaction));


        Map<Long, List<PostReaction>> reactionsByPostId = allReactions.stream()
                .collect(Collectors.groupingBy(reaction -> reaction.getPost().getId()));

        return posts.stream()
                .collect(Collectors.toMap(Post::getId, post -> {
                    List<PostReaction> currentPostReactions = reactionsByPostId.getOrDefault(post.getId(), Collections.emptyList());

                    Map<ReactionType, Long> reactionCounts = currentPostReactions.stream()
                            .collect(Collectors.groupingBy(
                                    PostReaction::getReactionType,
                                    Collectors.counting()
                            ));
                    for (ReactionType type : ReactionType.values()) {
                        reactionCounts.putIfAbsent(type, 0L);
                    }

                    boolean likedByCurrentUser = false;
                    boolean dislikedByCurrentUser = false;

                    PostReaction requesterReaction = reactionByRequesterByPostId.get(post.getId());

                    if (requesterReaction != null) {
                        likedByCurrentUser = requesterReaction.getReactionType() == ReactionType.LIKE;
                        dislikedByCurrentUser = requesterReaction.getReactionType() == ReactionType.DISLIKE;
                    }

                    return new ReactionResponse(reactionCounts, likedByCurrentUser, dislikedByCurrentUser);
                }));
    }

    public ReactionResponse getReactionResponseByPostId(Long memberId, Long postId) {
        return getReactionResponseByPostIdIn(memberId, List.of(postId)).get(postId);
    }

}
