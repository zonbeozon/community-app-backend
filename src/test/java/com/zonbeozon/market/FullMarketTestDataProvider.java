package com.zonbeozon.market;

import com.zonbeozon.common.ListFileLoaderTemplate;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.fetch.MarketFetchData;
import com.zonbeozon.market.fetch.MarketFetchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class FullMarketTestDataProvider implements MarketTestDataProvideHandler, MarketFetchTestDataProvideHandler {
    private final String dataPath;
    private final ListFileLoaderTemplate<TestMarketData> testMarketLoader = new ListFileLoaderTemplate<>(TestMarketData.class);
    private final Exchange exchange;
    private final List<Market> markets = new ArrayList<>();
    private Set<String> marketCodes;
    private MarketFetchResult marketFetchResult;

    public FullMarketTestDataProvider(
            Exchange exchange,
            String dataPath
    ) {
        this.exchange = exchange;
        this.dataPath = dataPath;
        init();
    }
    private void init() {
        List<TestMarketData> data = testMarketLoader.load(dataPath);
        initMarketCode(data);
        initMarkets(data);
        initMarketFetchResult(data);
    }

    private void initMarketCode(List<TestMarketData> data) {
        marketCodes = data.stream().map(TestMarketData::marketCode).collect(Collectors.toSet());
    }

    private void initMarkets(List<TestMarketData> data) {
        for (TestMarketData testMarketData : data) {
            Market market = MarketTestUtils.createMarket(
                    testMarketData.marketCode(),
                    exchange,
                    testMarketData.marketType(),
                    testMarketData.signedChangeRate()
            );
            MarketFiatMetric marketFiatMetric = MarketFiatMetric.create(
                    market,
                    testMarketData.fiatType(),
                    testMarketData.openingPrice(),
                    testMarketData.highPrice(),
                    testMarketData.lowPrice(),
                    testMarketData.tradePrice(),
                    testMarketData.signedChangePrice(),
                    testMarketData.accTradePrice()
            );
            MarketTestUtils.setMarketFiatMetrics(market, Set.of(marketFiatMetric));
            markets.add(market);
        }
    }

    private void initMarketFetchResult(List<TestMarketData> data) {
        marketFetchResult = new MarketFetchResult(
                exchange,
                data.stream().map(testMarketData -> new MarketFetchData(
                        testMarketData.marketCode(),
                        testMarketData.fiatType(),
                        testMarketData.openingPrice(),
                        testMarketData.highPrice(),
                        testMarketData.lowPrice(),
                        testMarketData.tradePrice(),
                        testMarketData.signedChangePrice(),
                        testMarketData.signedChangeRate(),
                        testMarketData.accTradePrice()
                )).toList()
        );
    }

    @Override
    public Set<String> getMarketCodes() {
        return marketCodes;
    }

    @Override
    public List<Market> getMarkets() {
        return markets.stream().map(MarketTestUtils::copyMarket).collect(Collectors.toList()); //깊은 복사
    }

    @Override
    public MarketFetchResult getMarketFetchResult() {
        return marketFetchResult;
    }
}
