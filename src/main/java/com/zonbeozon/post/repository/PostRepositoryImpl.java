package com.zonbeozon.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.CursorPageImpl;
import com.zonbeozon.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.member.domain.QMember.*;
import static com.zonbeozon.post.entity.QPost.*;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public CursorPage<Post> findCursorBasedPostsByChannel(BlogChannel channel, Long cursorPostId, int size) {
        List<Post> posts = queryFactory.selectFrom(post)
                .where(post.isDeleted.eq(false)
                        .and(post.channel.eq(channel))
                        .and(post.id.lt(cursorPostId))
                )
                .join(member).on(post.author.eq(member)).fetchJoin()
                .orderBy(post.id.desc())
                .limit(size + 1) //last 페이지인지 확인
                .fetch();

        List<Post> contentToReturn;
        Long nextCursorId;

        boolean hasNext = posts.size() > size;

        if(hasNext) {
            contentToReturn = posts.subList(0, size);
            nextCursorId = posts.get(size).getId();
        } else {
            contentToReturn = posts;
            nextCursorId = posts.getLast().getId();
        }

        Long totalElement = queryFactory
                .select(post.count()) // count 함수 사용
                .from(post)
                .where(post.isDeleted.isFalse().and(post.channel.eq(channel))) // 채널 조건
                .fetchOne();

        //조회되는 값 없을때 Null 대신 반환
        totalElement = totalElement == null ? 0L : totalElement;

        return new CursorPageImpl<>(contentToReturn, nextCursorId, totalElement, !hasNext, size);
    }
}
