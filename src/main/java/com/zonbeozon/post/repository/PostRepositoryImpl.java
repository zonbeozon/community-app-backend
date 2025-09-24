package com.zonbeozon.post.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.global.LongTypeCursorPage;
import com.zonbeozon.global.LongTypeCursorPageImpl;
import com.zonbeozon.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.zonbeozon.image.entity.QImage.image;
import static com.zonbeozon.post.entity.QPost.post;
import static com.zonbeozon.post.entity.QPostImage.postImage;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findById(Long id, PostFetchOptions options) {
        JPAQuery<Post> query = queryFactory.selectFrom(post);
        if (options.isWithAuthor()) {
            query.join(post.author).fetchJoin();
        }

        if (options.isWithChannel()) {
            query.join(post.channel).fetchJoin();
        }

        if (options.isWithImages()) {
            query.leftJoin(post.images, postImage).fetchJoin()
                    .leftJoin(postImage.image, image).fetchJoin();
        }

        query.where(post.id.eq(id));
        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public LongTypeCursorPage<Post> findCursorBasedPostsByChannelId(Long channelId, Long cursorPostId, int size, boolean inverted) {
        OrderSpecifier<?> order = inverted ? post.id.asc() : post.id.desc();
        BooleanExpression cursorCondition = null;
        if (cursorPostId != null) {
            cursorCondition = inverted ? post.id.gt(cursorPostId) : post.id.lt(cursorPostId);
        }

        List<Post> posts = queryFactory.selectFrom(post)
                .where(post.channel.id.eq(channelId), cursorCondition)
                .join(post.author).fetchJoin()
                .leftJoin(post.images, postImage).fetchJoin()
                .leftJoin(postImage.image).fetchJoin()
                .orderBy(order, postImage.id.asc())
                .limit(size + 1)
                .fetch();

        List<Post> contentToReturn;
        Long nextCursorId;

        boolean hasNext = posts.size() > size;

        if(posts.isEmpty()) {
            contentToReturn = new ArrayList<>();
            nextCursorId = null;
        } else if (hasNext) {
            contentToReturn = posts.subList(0, size);
            nextCursorId = posts.get(size).getId();
        } else {
            contentToReturn = posts;
            nextCursorId = posts.getLast().getId();
        }
        if (inverted) {
            Collections.reverse(contentToReturn);
        }

        Long totalElement = queryFactory
                .select(post.count())
                .from(post)
                .where(post.channel.id.eq(channelId))
                .fetchOne();

        totalElement = totalElement == null ? 0L : totalElement;

        return new LongTypeCursorPageImpl<>(contentToReturn, nextCursorId, totalElement, inverted ,!hasNext, posts.size());
    }

    @Override
    public Optional<Post> findByIdWithImages(Long postId) {
        Post result = queryFactory.selectFrom(post)
                .leftJoin(post.images, postImage).fetchJoin()
                .leftJoin(postImage.image).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
