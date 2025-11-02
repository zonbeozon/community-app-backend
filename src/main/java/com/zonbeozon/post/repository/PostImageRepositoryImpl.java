package com.zonbeozon.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.post.dto.PostImageCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.post.domain.QPost.post;
import static com.zonbeozon.post.domain.QPostImage.postImage;

@Repository
@RequiredArgsConstructor
public class PostImageRepositoryImpl implements PostImageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostImageCount> countImagesByPostIds(List<Long> postIds) {
        return queryFactory
                .select(Projections.constructor(PostImageCount.class,
                        post.id,
                        postImage.count()
                ))
                .from(post)
                .leftJoin(post.postImages, postImage)
                .where(post.id.in(postIds))
                .groupBy(post.id)
                .fetch();
    }
}
