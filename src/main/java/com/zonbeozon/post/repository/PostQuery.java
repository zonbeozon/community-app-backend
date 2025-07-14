package com.zonbeozon.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

import static com.zonbeozon.channel.entity.QChannel.channel;
import static com.zonbeozon.post.entity.QPost.post;

public class PostQuery {
    public static BooleanExpression isLatestPostCreatedAt() {
        return post.createdAt.eq(
                JPAExpressions.select(post.createdAt.max())
                        .from(post)
                        .where(post.channel.eq(channel))
        );
    }
}
