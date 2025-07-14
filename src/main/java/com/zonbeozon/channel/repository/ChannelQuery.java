package com.zonbeozon.channel.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import org.springframework.data.domain.Sort;

import static com.zonbeozon.channel.entity.QChannel.*;
import static com.zonbeozon.channel.entity.QChannelMember.channelMember;

public class ChannelQuery {
    public static BooleanExpression isNotDeleted() {
        return channel.isDeleted.eq(false);
    }

    public static BooleanExpression eqContentVisibility(ChannelContentVisibility contentVisibility) {
        return contentVisibility == null ? null : channel.setting.contentVisibility.eq(contentVisibility);
    }

    public static BooleanExpression eqJoinPolicy(ChannelJoinPolicy joinPolicy) {
        return joinPolicy == null ? null : channel.setting.joinPolicy.eq(joinPolicy);
    }

    public static BooleanExpression containsKeyword(String keyword) {
        return keyword == null ? null : channel.title.containsIgnoreCase(keyword); // 대소문자 구분 없이 포함 검색
    }

    public static OrderSpecifier<?> getOrderSpecifier(ChannelSort sort, Sort.Direction direction) {
        boolean asc = direction.isAscending();

        return switch (sort) {
            case MEMBER_COUNT -> asc ? channelMember.count().asc() : channelMember.count().desc();
            case CHANNEL_CREATED_AT -> asc ? channel.createdAt.asc() : channel.createdAt.desc();
        };
    }
}
