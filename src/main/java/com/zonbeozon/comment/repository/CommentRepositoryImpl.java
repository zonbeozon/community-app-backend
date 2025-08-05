package com.zonbeozon.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.comment.dto.CommentCountResult;
import com.zonbeozon.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.comment.entity.QComment.comment;
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> getCommentsByPostIdOrderByCreatedAtDesc(Long postId) {
        return queryFactory
                .selectFrom(comment)
                .join(comment.author).fetchJoin()
                .where(comment.post.id.eq(postId).and(comment.isDeleted.eq(false)))
                .orderBy(comment.createdAt.desc())
                .fetch();
    }

    @Override
    public List<CommentCountResult> countCommentsByPostIds(List<Long> postIds) {
        return queryFactory
                .select(Projections.constructor(CommentCountResult.class,
                        comment.post.id,
                        comment.id.count()
                ))
                .from(comment)
                .where(
                        comment.post.id.in(postIds),
                        comment.isDeleted.eq(false)
                )
                .groupBy(comment.post.id)
                .fetch();
    }
}
