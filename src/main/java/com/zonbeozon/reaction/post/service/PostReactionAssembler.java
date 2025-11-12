package com.zonbeozon.reaction.post.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReactionAssembler {
    private final PostReactionRepository postReactionRepository;

    public Map<Long, PersonalizedPostReactionDto> getPersonalizedInfoByPostIdIn(long requesterId, Collection<Long> postIds) {
        return postReactionRepository.findPersonalizedPostReactionByPostIdIn(requesterId, postIds).stream()
                .collect(Collectors.toMap(PersonalizedPostReactionDto::postId, Function.identity()));
    }

    public PersonalizedPostReactionDto getPersonalizedInfoByPostId(long requesterId, Long postId) {
        return postReactionRepository.findPersonalizedPostReactionByPostId(requesterId, postId).orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }
}
