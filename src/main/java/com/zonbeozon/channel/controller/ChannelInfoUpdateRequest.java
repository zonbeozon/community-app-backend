package com.zonbeozon.channel.controller;

import jakarta.validation.constraints.NotBlank;

public record ChannelInfoUpdateRequest(
        @NotBlank String title,
        @NotBlank String description
) {
}
