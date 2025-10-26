package com.zonbeozon.post.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.CursorPageImpl;
import com.zonbeozon.post.dto.PostCursor;
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
            query.leftJoin(post.postImages, postImage).fetchJoin()
                    .leftJoin(postImage.image, image).fetchJoin();
        }

        query.where(post.id.eq(id));
        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public CursorPage<Post, PostCursor> findCursorBasedPostsByChannelId(Long channelId, PostCursor postCursor, int size, boolean inverted) {
        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(inverted);
        BooleanExpression cursorCondition = cursorCondition(postCursor, inverted);

        List<Post> posts = queryFactory.selectFrom(post)
                .where(post.channel.id.eq(channelId), cursorCondition)
                .join(post.author).fetchJoin()
                .orderBy(orderSpecifiers)
                .limit(size + 1)
                .fetch();

        boolean hasNext = posts.size() > size;
        List<Post> contentToReturn = hasNext ? posts.subList(0, size) : new ArrayList<>(posts);
        PostCursor nextCursor = null;

        if (hasNext) {
            Post lastPost = contentToReturn.getLast();
            nextCursor = new PostCursor(lastPost.getCreatedAt(), lastPost.getId());
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

        return new CursorPageImpl<>(contentToReturn, nextCursor, totalElement, inverted, !hasNext);
    }

    private BooleanExpression cursorCondition(PostCursor cursor, boolean inverted) {
        if (cursor == null) {
            return null;
        }
        if (inverted) {
            // createdAt > cursor.createdAt OR (createdAt == cursor.createdAt AND postId > cursor.postId)
            return post.createdAt.gt(cursor.createdAt())
                    .or(post.createdAt.eq(cursor.createdAt()).and(post.id.gt(cursor.postId())));
        } else {
            // createdAt < cursor.createdAt OR (createdAt == cursor.createdAt AND postId < cursor.postId)
            return post.createdAt.lt(cursor.createdAt())
                    .or(post.createdAt.eq(cursor.createdAt()).and(post.id.lt(cursor.postId())));
        }
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(boolean inverted) {
        if (inverted) {
            return new OrderSpecifier<?>[]{post.createdAt.asc(), post.id.asc()};
        }
        return new OrderSpecifier<?>[]{post.createdAt.desc(), post.id.desc()};
    }

    @Override
    public Optional<Post> findByIdWithImages(Long postId) {
        Post result = queryFactory.selectFrom(post)
                .leftJoin(post.postImages, postImage).fetchJoin()
                .leftJoin(postImage.image).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
