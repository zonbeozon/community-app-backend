package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import org.springframework.web.client.RestClient;

class UpbitMarketFetcherTest extends AbstractMarketFetcherTest {
    private static final String REQUEST_URI = "https://api.upbit.com/v1/ticker";
    public UpbitMarketFetcherTest() {
        super(REQUEST_URI, Exchange.UPBIT);
    }

    @Override
    protected MarketFetcher createFetcher(RestClient.Builder restClientBuilder) {
        return new UpbitMarketFetcher(restClientBuilder);
    }
}
