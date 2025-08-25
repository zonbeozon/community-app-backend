package com.zonbeozon.channel.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.member.domain.QMember.member;
import static com.zonbeozon.member.domain.QMemberProfile.memberProfile;


@RequiredArgsConstructor
@Repository
public class ChannelMemberRepositoryImpl implements ChannelMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isKicked(ChannelMemberId id) {
        return queryFactory.selectOne()
                .from(channelMember)
                .where(channelMember.id.eq(id).and(channelMember.status.eq(ChannelMemberStatus.KICKED)))
                .fetchFirst() != null;
    }

    @Override
    public List<ChannelMember> findByIdIn(Collection<ChannelMemberId> ids, ChannelMemberFetchOptions options) {
        JPAQuery<ChannelMember> query = queryFactory.selectFrom(channelMember);
        applyChannelMemberFetchOptions(query, options);
        query.where(channelMember.id.in(ids));
        return query.fetch();
    }

    @Override
    public Optional<ChannelMember> findById(ChannelMemberId id, ChannelMemberFetchOptions options) {
        JPAQuery<ChannelMember> query = queryFactory.selectFrom(channelMember);
        applyChannelMemberFetchOptions(query, options);
        query.where(channelMember.id.eq(id));
        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Page<ChannelMember> findByChannelIdWithMemberOrderByCreatedAtDesc(Long channelId, ChannelMemberStatus status, Pageable pageable) {
        List<ChannelMember> content = queryFactory
                .selectFrom(channelMember)
                .join(channelMember.member, member).fetchJoin()
                .leftJoin(member.profile, memberProfile).fetchJoin()
                .leftJoin(memberProfile.image).fetchJoin()
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

    private void applyChannelMemberFetchOptions(JPAQuery<ChannelMember> query, ChannelMemberFetchOptions options) {
        if (options.isWithChannel()) {
            query.join(channelMember.channel).fetchJoin();
        }

        if (options.isWithMember()) {
            query.join(channelMember.member).fetchJoin();

            if (options.isWithMemberProfile()) {
                query.leftJoin(channelMember.member.profile, memberProfile).fetchJoin();
                query.leftJoin(memberProfile.image).fetchJoin();
            }
        }
    }
}
