package com.zonbeozon.market.update;

import com.zonbeozon.common.AbstractFetchBasedUpdater;
import com.zonbeozon.common.fetch.FetchManager;
import com.zonbeozon.fiat.ExpandToMultiFiat;
import com.zonbeozon.market.MarketHolder;
import com.zonbeozon.market.MarketHolderSupplier;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.fetch.MarketFetchContext;
import com.zonbeozon.market.fetch.MarketFetchData;
import com.zonbeozon.market.fetch.MarketFetchResultWrapper;
import com.zonbeozon.market.service.MarketFiatMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
class MarketUpdaterImpl extends AbstractFetchBasedUpdater<MarketFetchContext, MarketFetchResultWrapper> implements MarketUpdater {
    private final MarketHolderSupplier marketHolderSupplier;
    private final MarketFiatMetricService marketFiatMetricService;

    @Autowired
    public MarketUpdaterImpl(
            FetchManager<MarketFetchContext, MarketFetchResultWrapper> fetchManager,
            MarketHolderSupplier marketHolderSupplier,
            MarketFiatMetricService marketFiatMetricService
    ) {
        super(fetchManager);
        this.marketHolderSupplier = marketHolderSupplier;
        this.marketFiatMetricService = marketFiatMetricService;
    }

    @Override
    @Transactional
    @ExpandToMultiFiat
    public MarketHolder update() {
        MarketHolder marketHolder = marketHolderSupplier.get();
        updateUsingFetchResult(super.update(marketHolder), marketHolder);
        return marketHolder;
    }

    private void updateUsingFetchResult(MarketFetchResultWrapper fetchResultWrapper, MarketHolder marketHolder) {
        fetchResultWrapper.applyAll(
                fetchResult -> fetchResult.getData().forEach(
                        marketFetchData -> marketHolder.apply(
                                fetchResult.getExchange(),
                                marketFetchData.marketCode(),
                                market -> applyFetchData(market, marketFetchData)
                        )
                )
        );
    }

    private void applyFetchData(Market market, MarketFetchData marketFetchData) {
        MarketFiatMetric newMarketFiatMetric = marketFetchData.createMarketFiatMetric(market);
        Optional<MarketFiatMetric> optMarketFiatMetric = marketFiatMetricService.getMarketFiatMetricsByMarket(market).stream()
                .filter(marketFiatMetric -> marketFiatMetric.getFiatType() == newMarketFiatMetric.getFiatType())
                .findAny();
        market.updateSignedChangeRate(marketFetchData.signedChangeRate());
        if(optMarketFiatMetric.isPresent()) {
            optMarketFiatMetric.get().update(newMarketFiatMetric);
            return;
        }
        marketFiatMetricService.addMarketFiatMetric(newMarketFiatMetric);
    }
}
