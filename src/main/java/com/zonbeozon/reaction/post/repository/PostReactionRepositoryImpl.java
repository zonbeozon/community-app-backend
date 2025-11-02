package com.zonbeozon.reaction.post.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;
import com.zonbeozon.reaction.post.dto.PostReactionCountDto;
import com.zonbeozon.reaction.post.entity.ReactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.zonbeozon.post.domain.QPost.post;
import static com.zonbeozon.reaction.post.entity.QPostReaction.postReaction;

@Repository
@RequiredArgsConstructor
public class PostReactionRepositoryImpl implements CustomPostReactionRepository {
    private final JPAQueryFactory queryFactory;

    public List<PostReactionCountDto> countByPostIdIn(Collection<Long> postIds) {
        Expression<Long> likeCount = new CaseBuilder()
                .when(postReaction.reactionType.eq(ReactionType.LIKE))
                .then(1L)
                .otherwise(0L)
                .sum()
                .coalesce(0L);

        Expression<Long> dislikeCount = new CaseBuilder()
                .when(postReaction.reactionType.eq(ReactionType.DISLIKE))
                .then(1L)
                .otherwise(0L)
                .sum()
                .coalesce(0L);

        return queryFactory.select(
                Projections.constructor(PostReactionCountDto.class,
                        post.id,
                        likeCount,
                        dislikeCount
                ))
                .from(post)
                .leftJoin(post.reactions, postReaction)
                .where(post.id.in(postIds))
                .groupBy(post.id)
                .fetch();
    }

    public List<PersonalizedPostReactionDto> findPersonalizedPostReactionByPostIdIn(Long memberId, Collection<Long> postIds) {
        Expression<Boolean> likedByMember = postReaction.reactionType.eq(ReactionType.LIKE).coalesce(false);
        Expression<Boolean> dislikedByMember = postReaction.reactionType.eq(ReactionType.DISLIKE).coalesce(false);
        return queryFactory.select(
                Projections.constructor(PersonalizedPostReactionDto.class,
                        post.id,
                        likedByMember,
                        dislikedByMember
                ))
                .from(post)
                .leftJoin(post.reactions, postReaction).on(postReaction.author.id.eq(memberId))
                .where(post.id.in(postIds))
                .fetch();
    }


}
