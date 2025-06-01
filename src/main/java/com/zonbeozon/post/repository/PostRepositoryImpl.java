package com.zonbeozon.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QChannel.channel;
import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.post.entity.QPost.*;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private BooleanBuilder createDefaultBooleanBuilder() {
        return new BooleanBuilder()
                .and(post.isDeleted.eq(false));
    }

    @Override
    public Page<Post> findPagedPost(
            Long channelId,
            String keyword,
            int page,
            int size,
            PostSort sort,
            Sort.Direction direction
    ) {
        long offset = (long) page * size;

        BooleanBuilder whereClause = createDefaultBooleanBuilder()
                .and(post.channel.id.eq(channelId))
                .and(containsKeyword(keyword));

        List<Post> posts = queryFactory.selectFrom(post)
                .where(whereClause)
                .offset(offset)
                .orderBy(getOrderSpecifier(sort, direction))
                .limit(size)
                .fetch();

        Long total = queryFactory.select(post.count())
                .from(post)
                .where(whereClause)
                .fetchOne();

        long unboxedTotal = total == null ? 0L : total;

        return new PageImpl<>(posts, PageRequest.of(page, size, Sort.by(direction, sort.name())), unboxedTotal);
    }

    private BooleanExpression containsKeyword(String keyword) {
        return keyword == null ? null : channel.title.containsIgnoreCase(keyword); // 대소문자 구분 없이 포함 검색
    }

    private OrderSpecifier<?> getOrderSpecifier(PostSort sort, Sort.Direction direction) {
        boolean asc = direction.isAscending();

        return switch (sort) {
            case UPDATED_AT -> asc ? post.modifiedAt.asc() : post.modifiedAt.desc();
            case CREATED_AT -> asc ? post.createdAt.asc() : post.createdAt.desc();
        };
    }
}
