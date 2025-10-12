package com.zonbeozon.channel.repository.expression;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.dto.ChannelInfoDto;
import com.zonbeozon.channel.dto.ChannelInfoWithMembershipDto;
import com.zonbeozon.channel.dto.ChannelSettingDto;
import com.zonbeozon.channel.entity.QChannel;
import com.zonbeozon.channel.entity.QChannelMember;
import com.zonbeozon.channel.entity.QChannelProfile;
import com.zonbeozon.image.entity.QImage;
import com.zonbeozon.member.domain.QMember;
import com.zonbeozon.member.domain.QMemberProfile;

public class ChannelConstructorExpression {
    public static ConstructorExpression<ChannelInfoWithMembershipDto> channelInfoWithMembership(
            QChannel channel,
            QChannelProfile channelProfile,
            QImage channelProfileImage,
            QMember member,
            QChannelMember channelMember,
            QMemberProfile memberProfile,
            QImage memberProfileImage
    ) {
        return Projections.constructor(ChannelInfoWithMembershipDto.class,
                channelInfoDto(channel, channelProfile, channelProfileImage),
                channelMemberDto(member, channelMember, memberProfile, memberProfileImage)
        );
    }

    public static ConstructorExpression<ChannelMemberDto> channelMemberDto(
            QMember member,
            QChannelMember channelMember,
            QMemberProfile memberProfile,
            QImage image
    ) {
        return Projections.constructor(ChannelMemberDto.class,
                member.id,
                member.username,
                new CaseBuilder()
                        .when(memberProfile.isNotNull())
                        .then(image.id)
                        .otherwise((Long) null),
                new CaseBuilder()
                        .when(memberProfile.isNotNull())
                        .then(image.url)
                        .otherwise((String) null),
                member.role,
                channelMember.role
        );
    }

    public static ConstructorExpression<ChannelInfoDto> channelInfoDto(
            QChannel channel,
            QChannelProfile channelProfile,
            QImage image
    ) {
        return Projections.constructor(ChannelInfoDto.class,
                channel.id,
                channel.channelType,
                channel.title,
                channel.description,
                new CaseBuilder()
                        .when(channelProfile.isNotNull())
                        .then(image.id)
                        .otherwise((Long) null),
                new CaseBuilder()
                        .when(channelProfile.isNotNull())
                        .then(image.url)
                        .otherwise((String) null),
                Projections.constructor(ChannelSettingDto.class,
                        channel.setting.contentVisibility,
                        channel.setting.joinPolicy
                ),
                channel.memberCount
        );
    }
}
