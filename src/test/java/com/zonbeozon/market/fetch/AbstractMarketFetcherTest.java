package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.MarketFetchTestDataProvideRouter;
import com.zonbeozon.market.MarketTestDataProvideRouter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

abstract class AbstractMarketFetcherTest {
    private final String requestUri;
    private final MarketFetchTestDataProvideRouter fetchDataProvider = new MarketFetchTestDataProvideRouter();
    private final RestClient.Builder restClientBuilder = RestClient.builder();
    private final MockMarketServer server = new MockMarketServer(restClientBuilder);
    private final Exchange exchange;
    private MarketFetcher fetcher;

    public AbstractMarketFetcherTest(String requestUri, Exchange exchange) {
        this.requestUri = requestUri;
        this.exchange = exchange;
    }

    @BeforeEach
    void setUp() {
        server.initServer(exchange, requestUri);
        fetcher = createFetcher(server.getRestClientBuilder());
    }

    protected abstract MarketFetcher createFetcher(RestClient.Builder restClientBuilder);

    @Test
    @DisplayName("주어진 MarketCode로 요청을 하고 응답을 Dto로 변환하여 리턴한다.")
    void fetch_shouldReturnMappedMarketFetchResult_whenValidMarketCodesProvided() {
        MarketFetchResult marketFetchResult = fetcher.fetch(fetchDataProvider.getMarketCodes(exchange));
        Assertions.assertThat(marketFetchResult.getExchange()).isEqualTo(exchange);
        Assertions.assertThat(marketFetchResult.getData()).isEqualTo(fetchDataProvider.getMarketFetchResult(exchange).getData());
    }
}
