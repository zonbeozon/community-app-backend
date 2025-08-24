package com.zonbeozon.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.CursorPageImpl;
import com.zonbeozon.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.zonbeozon.image.entity.QImage.image;
import static com.zonbeozon.post.entity.QPost.post;
import static com.zonbeozon.post.entity.QPostImage.*;

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
    public CursorPage<Post> findCursorBasedPostsByChannelId(Long channelId, Long cursorPostId, int size) {

        BooleanBuilder whereClause = new BooleanBuilder()
                .and(post.channel.id.eq(channelId));

        if(cursorPostId != null) {
            whereClause.and(post.id.lt(cursorPostId));
        }

        List<Post> posts = queryFactory.selectFrom(post)
                .where(whereClause)
                .join(post.author).fetchJoin()
                .leftJoin(post.images, postImage).fetchJoin()
                .leftJoin(postImage.image).fetchJoin()
                .orderBy(post.id.desc(), postImage.id.asc())
                .limit(size + 1)
                .fetch();

        List<Post> contentToReturn;
        Long nextCursorId;

        boolean hasNext = posts.size() > size;

        if(posts.isEmpty()) {
            contentToReturn = List.of();
            nextCursorId = null;
        } else if (hasNext) {
            contentToReturn = posts.subList(0, size);
            nextCursorId = posts.get(size).getId();
        } else {
            contentToReturn = posts;
            nextCursorId = posts.getLast().getId();
        }

        Long totalElement = queryFactory
                .select(post.count()) // count 함수 사용
                .from(post)
                .where(whereClause) // 채널 조건
                .fetchOne();

        //조회되는 값 없을때 Null 대신 반환
        totalElement = totalElement == null ? 0L : totalElement;

        return new CursorPageImpl<>(contentToReturn, nextCursorId, totalElement, !hasNext, posts.size());
    }

    @Override
    public List<Post> findByIdInWithImagesAndAuthorAndChannel(Collection<Long> postIds) {
        return queryFactory.selectFrom(post)
                .leftJoin(post.author).fetchJoin()
                .leftJoin(post.images, postImage).fetchJoin()
                .leftJoin(postImage.image).fetchJoin()
                .leftJoin(post.channel).fetchJoin()
                .where(post.id.in(postIds))
                .fetch();
    }
}
