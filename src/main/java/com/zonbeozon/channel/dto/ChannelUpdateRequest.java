package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.validation.ChannelTitleProvider;
import com.zonbeozon.channel.validation.ValidChannelTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import static com.zonbeozon.channel.entity.Channel.*;

@ValidChannelTitle
public record ChannelUpdateRequest(
        @Schema(description = "채널 이름", minLength = MIN_TITLE_LENGTH, maxLength = MAX_TITLE_LENGTH, example = "My Channel")
        String title,
        @Schema(description = "채널 설명", maxLength = MAX_DESCRIPTION_LENGTH, example = "이 채널은...")
        @Size(min = MIN_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH, message = "채널 설명은 300자 이하여야 합니다.")
        String description,
        @Schema(description = "이미지id, null값 보낼시 채널 프로필이 삭제된다.")
        Long imageId,
        @Valid ChannelSettingRequest settings
) implements ChannelTitleProvider {
}
