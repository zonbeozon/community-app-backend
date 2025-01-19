package com.zonbeozon.communityapp.crpyto.domain.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
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
    private MarketType marketType;
}
