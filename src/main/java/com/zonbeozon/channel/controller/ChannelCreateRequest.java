package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.zonbeozon.channel.entity.Channel.*;

public record ChannelCreateRequest(
        @Schema(description = "채널 이름", minLength = 2, maxLength = 30, example = "My Channel")
        @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH, message = "채널 이름은 2자 이상 30자 이하여야 합니다.")
        String title,
        @Schema(description = "채널 설명", maxLength = 300, example = "이 채널은...")
        @Size(min = MIN_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH, message = "채널 설명은 300자 이하여야 합니다.")
        String description,
        @Schema(description = "채널 프로필 이미지 URL", example = "https://example.com/profile.png")
        @NotBlank(message = "채널 프로필 이미지는 필수입니다.")
        String profile,
        @Schema(description = "콘텐츠 공개 수준", example = "PUBLIC")
        @NotNull(message = "콘텐츠 공개 수준을 선택해야 합니다.")
        ChannelContentOpenLevel contentOpenLevel,
        @Schema(description = "채널 유형", example = "COMMUNITY_INFO")
        @NotNull(message = "채널 유형을 선택해야 합니다.")
        ChannelType channelType,
        @Schema(description = "가입 허용 수준", example = "OPEN")
        @NotNull(message = "가입 허용 수준을 선택해야 합니다.")
        ChannelJoinLevel joinLevel,
        @Schema(description = "검색 허용 수준", example = "PUBLIC")
        @NotNull(message = "검색 허용 수준을 선택해야 합니다.")
        ChannelSearchLevel searchLevel
) {
}
