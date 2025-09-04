package com.zonbeozon.channel.dto;

import org.springframework.lang.Nullable;

public record ChannelInfoWithRequesterResponse(
    ChannelInfoResponse channelInfo,
    /**
     * 채널에 가입되지 않은 상태라면 null
     */
    @Nullable ChannelMemberResponse requester
) {
}
