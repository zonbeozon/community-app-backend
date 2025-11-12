package com.zonbeozon.reaction.post.repository;

import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostReactionRepositoryCustom {
    List<PersonalizedPostReactionDto> findPersonalizedPostReactionByPostIdIn(Long memberId, Collection<Long> postIds);
    Optional<PersonalizedPostReactionDto> findPersonalizedPostReactionByPostId(Long memberId, Long postId);
}
