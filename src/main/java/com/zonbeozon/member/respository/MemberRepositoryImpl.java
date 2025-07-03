package com.zonbeozon.member.respository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.MemberStatus;
import com.zonbeozon.member.domain.ServerRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.member.domain.QMember.*;


@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> searchMemberByPartialUsername(String partialUsername, MemberSort sort, Sort.Direction direction, int page, int size) {
        long offset = (long) page * size;

        BooleanBuilder whereClause = createDefaultBooleanBuilder()
                .and(containsUsernameKeyword(partialUsername))
                .and(hasServerRole(ServerRole.USER));


        List<Member> members = queryFactory.selectFrom(member)
                .where(whereClause)
                .orderBy(getOrderSpecifier(sort, direction))
                .offset(offset)
                .limit(size)
                .fetch();

        Long total = queryFactory.select(member.count())
                .from(member)
                .where(whereClause)
                .fetchOne();

        long unboxedTotal = total == null ? 0L : total;

        return new PageImpl<>(members, PageRequest.of(page, size, Sort.by(direction, sort.name())), unboxedTotal);
    }

    private BooleanBuilder createDefaultBooleanBuilder() {
        return new BooleanBuilder()
                .and(member.status.eq(MemberStatus.ACTIVE));
    }

    private BooleanExpression containsUsernameKeyword(String keyword) {
        return keyword == null ? null : member.username.containsIgnoreCase(keyword); // 대소문자 구분 없이 포함 검색
    }

    private BooleanExpression hasServerRole(ServerRole serverRole) {
        return serverRole == null ? null : member.role.eq(serverRole);
    }

    private OrderSpecifier<?> getOrderSpecifier(MemberSort sort, Sort.Direction direction) {
        boolean asc = direction.isAscending();

        return switch (sort) {
            case CREATED_AT -> asc ? member.createdAt.asc() : member.createdAt.desc();
        };
    }
}
