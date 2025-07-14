package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelSearchScope;
import com.zonbeozon.channel.validation.ChannelSettingProvider;
import com.zonbeozon.channel.validation.ChannelTitleProvider;
import com.zonbeozon.channel.validation.ValidChannelSetting;
import com.zonbeozon.channel.validation.ValidChannelTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.zonbeozon.channel.entity.Channel.*;

@ValidChannelTitle
@ValidChannelSetting
public record ChannelUpdateRequest(
        @Schema(description = "채널 이름", minLength = MIN_TITLE_LENGTH, maxLength = MAX_TITLE_LENGTH, example = "My Channel")
        String title,
        @Schema(description = "채널 설명", maxLength = MAX_DESCRIPTION_LENGTH, example = "이 채널은...")
        @Size(min = MIN_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH, message = "채널 설명은 300자 이하여야 합니다.")
        String description,
        @NotBlank(message = "채널 프로필 이미지는 필수입니다.")
        String profile,
        @NotNull(message = "콘텐츠 공개 수준을 선택해야 합니다.")
        ChannelContentVisibility contentVisibility,
        @NotNull(message = "가입 허용 수준을 선택해야 합니다.")
        ChannelJoinPolicy joinPolicy,
        @NotNull(message = "검색 허용 수준을 선택해야 합니다.")
        ChannelSearchScope searchScope
) implements ChannelSettingProvider, ChannelTitleProvider {
}
