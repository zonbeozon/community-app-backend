package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MarketAddRequest(
        @NotNull(message = "currency id는 필수 필드입니다.")
        Long currencyId,
        @NotBlank(message = "market pair는 필수 필드입니다.")
        String pair,
        @NotBlank(message = "거래소 이름은 필수 필드입니다.")
        String exchangeName
) {
}
