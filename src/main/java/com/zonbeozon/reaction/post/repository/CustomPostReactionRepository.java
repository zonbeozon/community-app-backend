package com.zonbeozon.reaction.post.repository;

import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;
import com.zonbeozon.reaction.post.dto.PostReactionCountDto;

import java.util.Collection;
import java.util.List;

public interface CustomPostReactionRepository {
    List<PostReactionCountDto> countByPostIdIn(Collection<Long> postIds);
    List<PersonalizedPostReactionDto> findPersonalizedPostReactionByPostIdIn(Long memberId, Collection<Long> postIds);
}
