package com.zonbeozon.channel.service.dto;

import java.util.List;

public record JoinedChannelResponseWrapper(
    List<JoinedChannelResponse> channels,
    int size
) {
}
