package com.zonbeozon.channel.service.dto;

import java.util.List;

public record ChannelResponseWrapper(
    List<ChannelResponse> channels,
    int size
) {
}
