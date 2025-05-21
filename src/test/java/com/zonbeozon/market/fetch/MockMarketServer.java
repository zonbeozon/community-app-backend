package com.zonbeozon.market.fetch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.MarketFetchTestDataProvideRouter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

public class MockMarketServer {
    private final RestClient.Builder restClientBuilder;
    private final MockRestServiceServer server;
    private final MarketFetchTestDataProvideRouter marketFetchTestDataProvider;
    private final ObjectMapper objectMapper;

    public MockMarketServer(RestClient.Builder restClientBuilder) {
        this.marketFetchTestDataProvider = new MarketFetchTestDataProvideRouter();
        this.restClientBuilder = restClientBuilder;
        this.server = MockRestServiceServer.bindTo(restClientBuilder).build();
        this.objectMapper = new ObjectMapper();
    }

    public void initServer(Exchange exchange, String expectedUri) {
        String expectedParamValue = String.join(",", marketFetchTestDataProvider.getMarketCodes(exchange));
        server.expect(requestTo(expectedUri))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("markets", expectedParamValue))
                .andRespond(MockRestResponseCreators.withSuccess(createDummyJsonResponse(exchange), MediaType.APPLICATION_JSON));
    }

    public RestClient.Builder getRestClientBuilder() {
        return restClientBuilder;
    }

    private String createDummyJsonResponse(Exchange exchange) {
        MarketFetchResult marketFetchResult = marketFetchTestDataProvider.getMarketFetchResult(exchange);
        List<UpbitMarketFetchResponse> marketFetchResponse = marketFetchResult.getData().stream().map(marketFetchData -> new UpbitMarketFetchResponse(
                marketFetchData.marketCode(),
                marketFetchData.openingPrice(),
                marketFetchData.highPrice(),
                marketFetchData.lowPrice(),
                marketFetchData.tradePrice(),
                marketFetchData.signedChangePrice(),
                marketFetchData.signedChangeRate(),
                marketFetchData.accTradePrice()
        )).toList();
        try {
            return objectMapper.writeValueAsString(marketFetchResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
