package com.zonbeozon.market;

import com.zonbeozon.common.ListFileLoaderTemplate;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;

import java.util.*;
import java.util.stream.Collectors;

public class BasicMarketTestDataProvider implements MarketTestDataProvideHandler {
    private final String dataPath;
    private final ListFileLoaderTemplate<TestMarketData> testMarketLoader = new ListFileLoaderTemplate<>(TestMarketData.class);
    private final Exchange exchange;
    private final List<Market> markets = new ArrayList<>();
    private Set<String> marketCodes;

    public BasicMarketTestDataProvider(
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
    }

    private void initMarketCode(List<TestMarketData> data) {
        marketCodes = data.stream().map(TestMarketData::marketCode).collect(Collectors.toSet());
    }

    private void initMarkets(List<TestMarketData> data) {
        for (TestMarketData testMarketData : data) {
            Market market = Market.create(
                    testMarketData.marketCode(),
                    exchange,
                    testMarketData.marketType(),
                    null
            );
            markets.add(market);
        }
    }

    @Override
    public Set<String> getMarketCodes() {
        return marketCodes;
    }

    @Override
    public List<Market> getMarkets() {
        return markets.stream().map(MarketTestUtils::copyMarket).collect(Collectors.toList()); //깊은 복사
    }
}
