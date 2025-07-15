package com.zonbeozon.channel.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.zonbeozon.channel.enums.ChannelMemberStatus;

import static com.zonbeozon.channel.entity.QChannelMember.channelMember;

public class ChannelMemberQuery {
    public static BooleanExpression isActive() {
        return channelMember.status.eq(ChannelMemberStatus.ACTIVE);
    }
}
