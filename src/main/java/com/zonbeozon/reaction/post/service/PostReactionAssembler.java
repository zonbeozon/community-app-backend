package com.zonbeozon.reaction.post.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;
import com.zonbeozon.reaction.post.dto.PostReactionCountDto;
import com.zonbeozon.reaction.post.dto.PostReactionCountWithPersonalizedDto;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReactionAssembler {
    private final PostReactionRepository postReactionRepository;

    /**
     * @return PostId가 키로 된 Post별 Reaction 개수
     */
    public Map<Long, PostReactionCountDto> getReactionCountByPostIdIn(Collection<Long> postIds) {
        List<PostReactionCountDto> counts = postReactionRepository.countByPostIdIn(postIds);
        if(counts.size() != postIds.size()) throw new NotFoundException(ErrorCode.POST_NOT_FOUND);
        return counts.stream().collect(Collectors.toMap(PostReactionCountDto::postId, Function.identity()));
    }

    public Map<Long, PostReactionCountWithPersonalizedDto> getReactionCountWithPersonalizedInfoByPostIdIn(Long requesterId, Collection<Long> postIds) {
        Map<Long, PostReactionCountDto> postReactionCount = getReactionCountByPostIdIn(postIds);
        return postReactionRepository.findPersonalizedPostReactionByPostIdIn(requesterId, postIds).stream()
                .map(personalizedInfo -> {
                    PostReactionCountDto count = postReactionCount.get(personalizedInfo.postId());
                    return new PostReactionCountWithPersonalizedDto(
                            personalizedInfo.postId(),
                            count.likeCount(),
                            count.dislikeCount(),
                            personalizedInfo.likedByRequester(),
                            personalizedInfo.dislikedByRequester()
                    );
                }).collect(Collectors.toMap(PostReactionCountWithPersonalizedDto::postId, Function.identity()));
    }
}
