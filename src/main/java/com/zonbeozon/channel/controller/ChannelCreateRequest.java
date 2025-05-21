package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.entity.Channel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChannelCreateRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull Channel.OpenLevel openLevel,
        @NotNull Channel.Type type
) {
}
