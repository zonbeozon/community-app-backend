package com.zonbeozon.channel.service.dto;

public record ChannelUpdateInfoCommand(
        String title,
        String description,
        String profile
) {
}
