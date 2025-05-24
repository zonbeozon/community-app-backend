package com.zonbeozon.channel.controller;

public record ChannelInfoUpdateRequest(
        String title,
        String description,
        String profile
) {
}
