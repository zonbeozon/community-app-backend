package com.zonbeozon.channel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ChannelMemberCount {
    private Long channelId;
    private Long count;
}
