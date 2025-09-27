package com.zonbeozon.channel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.Nullable;

public record ChannelViewDto(
        ChannelInfoDto channelInfo,
        boolean isJoined,
        @Nullable ChannelMemberDto membership
) {
}
