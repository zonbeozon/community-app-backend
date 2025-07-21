package com.zonbeozon.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;

import static com.zonbeozon.post.entity.QPost.post;

public class PostQuery {
    public static BooleanExpression isNotDeleted() {
        return post.isDeleted.eq(false);
    }
}
