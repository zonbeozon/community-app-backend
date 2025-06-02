package com.zonbeozon.market.update;

import com.zonbeozon.market.MarketHolder;
import com.zonbeozon.market.SimpleMarketHolder;
import com.zonbeozon.market.fetch.MarketFetchData;
import com.zonbeozon.market.fetch.MarketFetchManager;
import com.zonbeozon.market.service.MarketEntityQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MarketUpdater {
    private final MarketEntityQueryService marketEntityQueryService;
    private final MarketFetchManager marketFetchManager;
    private final MarketFetchDataHandler marketFetchDataHandler;

    public void update() {
        MarketHolder marketHolder = new SimpleMarketHolder(new HashSet<>(marketEntityQueryService.getAllMarkets()));
        List<MarketFetchData> fetchData = marketFetchManager.fetch(marketHolder);
        fetchData.forEach(marketFetchDataHandler::handle);
    }
}
