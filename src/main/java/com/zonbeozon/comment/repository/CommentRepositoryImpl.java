package com.zonbeozon.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.comment.dto.CommentCountResult;
import com.zonbeozon.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.comment.entity.QComment.comment;
import static com.zonbeozon.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> getCommentsByPostIdOrderByCreatedAtDesc(Long postId) {
        return queryFactory
                .selectFrom(comment)
                .join(comment.author).fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(comment.createdAt.desc())
                .fetch();
    }

    @Override
    public List<CommentCountResult> countCommentsByPostIds(List<Long> postIds) {
        return queryFactory
                .select(Projections.constructor(CommentCountResult.class,
                        post.id,
                        comment.count()
                ))
                .from(post)
                .leftJoin(comment).on(comment.post.id.eq(post.id))
                .where(post.id.in(postIds))
                .groupBy(post)
                .fetch();
    }
}
