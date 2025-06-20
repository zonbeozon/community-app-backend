package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.zonbeozon.channel.entity.Channel.*;

public record ChannelCreateRequest(
        @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH, message = "채널 이름은 2자 이상 30자 이하여야 합니다.")
        String title,
        @Size(min = MIN_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH, message = "채널 설명은 300자 이하여야 합니다.")
        String description,
        @NotBlank(message = "채널 프로필 이미지는 필수입니다.")
        String profile,
        @NotNull(message = "콘텐츠 공개 수준을 선택해야 합니다.")
        ChannelContentOpenLevel contentOpenLevel,
        @NotNull(message = "채널 유형을 선택해야 합니다.")
        ChannelType channelType,
        @NotNull(message = "가입 허용 수준을 선택해야 합니다.")
        ChannelJoinLevel joinLevel,
        @NotNull(message = "검색 허용 수준을 선택해야 합니다.")
        ChannelSearchLevel searchLevel
) {
}
