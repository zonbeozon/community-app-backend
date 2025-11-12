package com.zonbeozon.post.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static com.zonbeozon.post.domain.metric.QPostMetric.postMetric;

@Repository
@RequiredArgsConstructor
public class PostMetricRepositoryImpl implements PostMetricRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public long updateViewCounts(Map<Long, Long> viewCounts) {
        if (viewCounts == null || viewCounts.isEmpty()) {
            return 0;
        }

        CaseBuilder caseBuilder = new CaseBuilder();
        NumberExpression<Long> viewCountCase = postMetric.viewCount;
        for (Map.Entry<Long, Long> entry : viewCounts.entrySet()) {
            Long postId = entry.getKey();
            Long incrementValue = entry.getValue();

            viewCountCase = caseBuilder
                    .when(postMetric.id.eq(postId))
                    .then(postMetric.viewCount.add(incrementValue))
                    .otherwise(viewCountCase);
        }
        return queryFactory
                .update(postMetric)
                .set(postMetric.viewCount, viewCountCase)
                .where(postMetric.id.in(viewCounts.keySet()))
                .execute();
    }

    @Override
    public void updateLikeCount(Long postId, Long delta) {
        queryFactory.update(postMetric)
                .set(postMetric.likeCount, postMetric.likeCount.add(delta))
                .where(postMetric.id.eq(postId))
                .execute();
    }

    @Override
    public void updateDislikeCount(Long postId, Long delta) {
        queryFactory.update(postMetric)
                .set(postMetric.dislikeCount, postMetric.dislikeCount.add(delta))
                .where(postMetric.id.eq(postId))
                .execute();
    }

    @Override
    public void updateCommentCount(Long postId, Long delta) {
        queryFactory.update(postMetric)
                .set(postMetric.commentCount, postMetric.commentCount.add(delta))
                .where(postMetric.id.eq(postId))
                .execute();
    }
}
