package com.zonbeozon.communityapp.crpyto.fetch.bithumb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BithumbMarketRequest {
    @NotBlank(message = "Market code cannot be blank")
    @JsonProperty("market")
    private String marketCode;
    @NotBlank(message = "korean_name cannot be blank")
    @JsonProperty("korean_name")
    private String koreanName;
    @NotBlank(message = "english_name cannot be blank")
    @JsonProperty("english_name")
    private String englishName;
}
