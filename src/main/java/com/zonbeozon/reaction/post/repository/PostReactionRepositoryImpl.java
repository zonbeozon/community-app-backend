package com.zonbeozon.reaction.post.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;
import com.zonbeozon.reaction.post.entity.ReactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.zonbeozon.post.domain.QPost.post;
import static com.zonbeozon.reaction.post.entity.QPostReaction.postReaction;

@Repository
@RequiredArgsConstructor
public class PostReactionRepositoryImpl implements PostReactionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

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

    @Override
    public Optional<PersonalizedPostReactionDto> findPersonalizedPostReactionByPostId(Long memberId, Long postId) {
        Expression<Boolean> likedByMember = postReaction.reactionType.eq(ReactionType.LIKE).coalesce(false);
        Expression<Boolean> dislikedByMember = postReaction.reactionType.eq(ReactionType.DISLIKE).coalesce(false);
        return Optional.ofNullable(
                queryFactory.select(
                        Projections.constructor(PersonalizedPostReactionDto.class,
                                post.id,
                                likedByMember,
                                dislikedByMember
                        ))
                .from(post)
                .leftJoin(post.reactions, postReaction).on(postReaction.author.id.eq(memberId))
                .where(post.id.eq(postId))
                .fetchOne()
        );
    }

}
