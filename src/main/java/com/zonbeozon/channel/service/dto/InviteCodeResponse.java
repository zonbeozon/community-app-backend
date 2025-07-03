package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.member.service.dto.MemberResponse;

public record InviteCodeResponse (
    String code,
    Long channelId,
    String title,
    String profile,
    String description,
    ChannelType channelType,
    MemberResponse inviter
) {

    public static InviteCodeResponse with(String code, Channel channel, MemberResponse inviter) {
        return new InviteCodeResponse(
                code,
                channel.getId(),
                channel.getTitle(),
                channel.getProfile(),
                channel.getDescription(),
                channel.getType(),
                inviter
        );
    }

}