package com.zonbeozon.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
