package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.validation.ChannelSettingProvider;
import com.zonbeozon.channel.validation.ValidChannelSetting;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "채널 설정 정보")
public record ChannelSettingRequest(
        @Schema(description = "채널 컨첸츠 공개 수준", example = "PUBLIC")
        @NotNull(message = "채널 컨텐츠 공개 수준을 선택해야 합니다.")
        ChannelContentVisibility contentVisibility,

        @Schema(description = "가입 허용 수준", example = "OPEN")
        @NotNull(message = "가입 허용 수준을 선택해야 합니다.")
        ChannelJoinPolicy joinPolicy
) implements ChannelSettingProvider { }