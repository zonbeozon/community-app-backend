package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import org.springframework.web.client.RestClient;

class BinanceMarketFetcherTest extends AbstractMarketFetcherTest {
    private static final String REQUEST_URI = "https://api.binance.com/api/v3/ticker/tradingDay";

    public BinanceMarketFetcherTest() {
        super(REQUEST_URI, Exchange.BINANCE);
    }

    @Override
    protected MarketFetcher createFetcher(RestClient.Builder restClientBuilder) {
        return new BinanceMarketFetcher(restClientBuilder);
    }
}
