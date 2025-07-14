package com.zonbeozon.channel.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zonbeozon.channel.entity.QChannelMember.*;

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
}
