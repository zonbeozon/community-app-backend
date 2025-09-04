package com.zonbeozon.channel.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlogChannelRepositoryImpl implements BlogChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;

}
