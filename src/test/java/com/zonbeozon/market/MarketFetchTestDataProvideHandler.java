package com.zonbeozon.market;

import com.zonbeozon.market.fetch.MarketFetchResult;

import java.util.Set;

public interface MarketFetchTestDataProvideHandler {
    MarketFetchResult getMarketFetchResult();
    Set<String> getMarketCodes()
;}
