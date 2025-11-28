package com.zonbeozon.channel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ChannelViewDto(
        ChannelInfoDto channelInfo,
        boolean isJoined,
        @JsonInclude(JsonInclude.Include.NON_NULL) ChannelMemberDto membership
) {
}
