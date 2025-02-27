package com.zonbeozon.communityapp.crpyto.controller.dto.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record DescriptionUpdateRequest(
        @JsonProperty("description")
        @NotBlank(message = "description이 null이나 공백입니다.")
        String description
) {
}
