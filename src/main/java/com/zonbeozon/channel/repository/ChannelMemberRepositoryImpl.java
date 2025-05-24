package com.zonbeozon.channel.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMemberStatus;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.zonbeozon.channel.entity.QChannelMember.*;

@RequiredArgsConstructor
@Repository
public class ChannelMemberRepositoryImpl implements ChannelMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private BooleanBuilder createDefaultBooleanBuilder() {
        return new BooleanBuilder()
                .and(channelMember.status.eq(ChannelMemberStatus.ACTIVE));
    }

    @Override
    public boolean isKicked(Member member, Channel channel) {
        return queryFactory.selectOne()
                .from(channelMember)
                .where(
                        channelMember.status.eq(ChannelMemberStatus.KICKED),
                        channelMember.member.eq(member),
                        channelMember.channel.eq(channel)
                )
                .fetchFirst() != null;
    }
}
