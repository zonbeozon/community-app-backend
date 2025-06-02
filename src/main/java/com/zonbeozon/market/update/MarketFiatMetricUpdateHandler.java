package com.zonbeozon.market.update;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.fiat.service.ConversionRateService;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.fetch.MarketFetchData;
import com.zonbeozon.market.service.MarketEntityQueryService;
import com.zonbeozon.market.service.MarketFiatMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MarketFiatMetricUpdateHandler implements MarketFetchDataHandler {
    private final MarketFiatMetricService marketFiatMetricService;
    private final MarketEntityQueryService marketEntityQueryService;

    @Override
    @Transactional
    public void handle(MarketFetchData marketFetchData) {
        Optional<MarketFiatMetric> toBeUpdate = marketFiatMetricService.getMarketFiatMetricByMarketIdAndFiatType(
                marketFetchData.marketId(),
                marketFetchData.fiatType()
        );
        Market market = marketEntityQueryService.getMarketByIdOrThrow(marketFetchData.marketId());
        MarketFiatMetric newMarketFiatMetric = marketFetchData.toMarketFiatMetric(market);
        if(toBeUpdate.isPresent()) {
            toBeUpdate.get().update(newMarketFiatMetric);
            return;
        }
        marketFiatMetricService.addMarketFiatMetric(newMarketFiatMetric);
    }
}
