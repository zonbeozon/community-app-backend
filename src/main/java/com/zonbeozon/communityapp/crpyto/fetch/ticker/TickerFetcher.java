package com.zonbeozon.communityapp.crpyto.fetch.ticker;

import com.zonbeozon.communityapp.crpyto.fetch.ticker.dto.TickerFetchResult;

import java.util.Collection;

public interface TickerFetcher {
    TickerFetchResult fetch(Collection<String> marketCodes);
    String getExchangeName();
}
