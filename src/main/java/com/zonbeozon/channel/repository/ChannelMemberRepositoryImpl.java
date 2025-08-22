package com.zonbeozon.channel.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.member.domain.QMember.member;


@RequiredArgsConstructor
@Repository
public class ChannelMemberRepositoryImpl implements ChannelMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isKicked(Member member, Channel channel) {
        return queryFactory.selectOne()
                .from(channelMember)
                .where(
                        channelMember.member.eq(member),
                        channelMember.channel.eq(channel),
                        channelMember.status.eq(ChannelMemberStatus.KICKED)
                )
                .fetchFirst() != null;
    }

    @Override
    public Page<ChannelMember> findByChannelIdWithMemberOrderByCreatedAtDesc(Long channelId, ChannelMemberStatus status, Pageable pageable) {
        List<ChannelMember> content = queryFactory
                .selectFrom(channelMember)
                .join(channelMember.member, member).fetchJoin()
                .where(
                        channelMember.channel.id.eq(channelId),
                        channelMember.status.eq(status)
                )
                .orderBy(channelMember.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(channelMember.count())
                .from(channelMember)
                .where(
                        channelMember.channel.id.eq(channelId),
                        channelMember.status.eq(ChannelMemberStatus.ACTIVE)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
