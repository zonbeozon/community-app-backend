package com.zonbeozon.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.comment.dto.CommentCountResult;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.zonbeozon.comment.entity.QComment.comment;
import static com.zonbeozon.image.entity.QImage.image;
import static com.zonbeozon.post.entity.QPost.post;
import static com.zonbeozon.post.entity.QPostImage.postImage;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentsByPostIdOrderByCreatedAtDesc(Long postId) {
        return queryFactory
                .selectFrom(comment)
                .join(comment.author).fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(comment.createdAt.desc())
                .fetch();
    }

    @Override
    public List<CommentCountResult> countCommentsByPostIds(Collection<Long> postIds) {
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

    @Override
    public Optional<Comment> findById(Long id, CommentFetchOptions options) {
        JPAQuery<Comment> query = queryFactory.selectFrom(comment);
        if (options.isWithAuthor()) {
            query.join(post.author).fetchJoin();
        }

        if (options.isWithPost()) {
            query.join(post.channel).fetchJoin();
        }

        return Optional.ofNullable(query.where(comment.id.eq(id)).fetchOne());
    }
}
