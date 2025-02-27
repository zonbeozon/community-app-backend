package com.zonbeozon.communityapp.crpyto.service.market;

import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;

public interface MarketTypeResolver {
    MarketType resolveFromPair(String exchangeName, String pair);
    MarketType resolveFromMarketCode(String marketCode);
}
