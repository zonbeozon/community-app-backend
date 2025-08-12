package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.*;
import com.zonbeozon.channel.validation.ChannelSettingProvider;
import com.zonbeozon.channel.validation.ChannelTitleProvider;
import com.zonbeozon.channel.validation.ValidChannelSetting;
import com.zonbeozon.channel.validation.ValidChannelTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.zonbeozon.channel.entity.Channel.*;

@ValidChannelTitle
public record ChannelCreateRequest(
        @Schema(example = "BLOG")
        @NotNull(message = "채널 유형을 선택해야 합니다.")
        ChannelType channelType,
        @Schema(minLength = MIN_TITLE_LENGTH, maxLength = MAX_TITLE_LENGTH, example = "My Channel")
        String title,
        @Schema(maxLength = MAX_DESCRIPTION_LENGTH, example = "이 채널은...")
        @Size(max = MAX_DESCRIPTION_LENGTH, message = "{channel.description.length}")
        String description,
        @Schema(example = "1")
        Long imageId,
        @Valid
        ChannelSettingRequest settings
) implements ChannelTitleProvider {
        public ChannelCreateCommand toCommand(ChannelCreatorType creatorType) {
                return new ChannelCreateCommand(
                        channelType, title, description, imageId, settings.visibility(), settings.joinPolicy(), creatorType
                );
        }
}
