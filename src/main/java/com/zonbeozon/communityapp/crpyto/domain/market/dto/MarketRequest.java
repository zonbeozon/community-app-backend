package com.zonbeozon.communityapp.crpyto.domain.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketRequest {
    private String marketCode;
    private String koreanName;
    private String englishName;
}
