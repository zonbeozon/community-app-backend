package com.zonbeozon.market.update;

import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.fetch.MarketFetchData;
import com.zonbeozon.market.service.MarketEntityQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MarketUpdateHandler implements MarketFetchDataHandler {
    private final MarketEntityQueryService marketEntityQueryService;

    @Override
    @Transactional
    public void handle(MarketFetchData marketFetchData) {
        Market market = marketEntityQueryService.getMarketByIdOrThrow(marketFetchData.marketId());
        market.updateSignedChangeRate(marketFetchData.signedChangeRate());
    }
}
