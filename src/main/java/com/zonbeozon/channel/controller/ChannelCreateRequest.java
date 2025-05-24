package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChannelCreateRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String profile,
        @NotNull ChannelContentOpenLevel contentOpenLevel,
        @NotNull ChannelType channelType,
        @NotNull ChannelJoinLevel joinLevel,
        @NotNull ChannelSearchLevel searchLevel
) {
}
