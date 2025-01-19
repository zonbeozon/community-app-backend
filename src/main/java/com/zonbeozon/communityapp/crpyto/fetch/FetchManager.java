package com.zonbeozon.communityapp.crpyto.fetch;

import com.zonbeozon.communityapp.crpyto.fetch.dto.MarketFetchResult;
import com.zonbeozon.communityapp.crpyto.fetch.dto.TickerFetchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FetchManager {
    private final List<MarketFetcher> marketFetchers;
    private final List<TickerFetcher> tickerFetchers;

    public List<MarketFetchResult> fetchMarkets() {
        return marketFetchers.stream().map(MarketFetcher::fetch).toList();
    }

    public List<TickerFetchResult> fetchTickers() {
        return tickerFetchers.stream().map(TickerFetcher::fetch).toList();
    }
}
