package com.zonbeozon.market.service;

import com.zonbeozon.market.entity.Market;

import java.util.List;

public interface MarketEntityQueryService {
    Market getMarketByIdOrThrow(Long marketId);
    List<Market> getAllMarkets();
}
