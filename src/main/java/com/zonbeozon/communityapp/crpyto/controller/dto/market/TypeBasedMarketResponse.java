package com.zonbeozon.communityapp.crpyto.controller.dto.market;

import com.zonbeozon.communityapp.crpyto.controller.dto.ticker.TickerResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeBasedMarketResponse {
    private TickerResponse krw;
    private TickerResponse usdt;
    private TickerResponse btc;
}
