package com.zonbeozon.communityapp.crpyto.fetch.dto;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MarketFetchResult {
    private List<Market> markets;
    private String exchangeName;
}
